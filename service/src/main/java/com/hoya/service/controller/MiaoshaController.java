package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.core.exception.ServerExceptionServerError;
import com.hoya.service.annotation.AccessLimit;
import com.hoya.service.commons.rabbitmq.MQSender;
import com.hoya.service.commons.rabbitmq.MiaoshaMessage;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.GoodsKeyPrefix;
import com.hoya.service.commons.zk.ProductSoldOutWatcher;
import com.hoya.service.commons.zk.ZkApi;
import com.hoya.service.constant.CustomerConstant;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.util.CommonMethod;
import com.hoya.service.util.ProductSoutOutMap;
import com.hoya.service.util.ValidMSTime;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisClient redisClient;

    @Autowired
    MQSender mqSender;

    @Autowired
    private ZkApi zooKeeper;

    @Autowired
    ProductSoldOutWatcher productSoldOutWatcher;

    // 初始化预热商品
    @PostConstruct
    public void preheat() {
        try {
            List<GoodsVo> list = goodsService.goodsVoList();
            for (GoodsVo goodsVo : list) {
                redisClient.set(GoodsKeyPrefix.getMiaoshaGoodsStock, String.valueOf(goodsVo.getId()), goodsVo.getStockCount());
            }
        } catch (Exception e) {
            log.error("商品预热失败:{}", e);
            return;
        }
    }

    /**
     * 创建秒杀路径
     *
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @GetMapping("/path")
    public String getMiaoshaPath(MiaoShaUser user, @RequestParam() Long goodsId) {
        if (null == user) {
            throw new ServerExceptionForbidden("需要登录");
        }

        try {
            MiaoShaUserVo userVo = new MiaoShaUserVo();
            BeanUtils.copyProperties(user, userVo);
            return miaoshaService.createMiaoshaPath(userVo, goodsId);
        } catch (Exception e) {
            log.error("秒杀失败：{}", e);
            throw new ServerExceptionServerError("秒杀失败");
        }
    }

    /**
     * 开始秒杀
     * FIXME: 如何防止用户不停的刷新导致消息队列充满同一个用户的秒杀信息
     *
     * @param user
     * @param path
     * @param goodsId
     */
    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @PostMapping("/{path}/cut-the-hand")
    public Integer cutTheHand(MiaoShaUser user, @PathVariable("path") String path, @RequestParam long goodsId) {
        if (user == null) {
            throw new ServerExceptionForbidden("请登录");
        }

        // 验证path
        MiaoShaUserVo miaoShaUserVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, miaoShaUserVo);
        boolean check = miaoshaService.checkPath(miaoShaUserVo, goodsId, path);
        if (!check) {
            throw new ServerExceptionForbidden("秒杀失败,路径过期");
        }

        // 本机内存判断是否秒杀完毕，可以部分减少和redis的交互
        Boolean soldOut = ProductSoutOutMap.isSoldOut(goodsId);
        if (null != soldOut) {
            throw new ServerExceptionForbidden("秒杀完毕了");
        }

        // redis zookeeper 判断

        // 设置redis排队,超时时间，使用SETNX命令(分布式锁),如果返回false已经在排队了或者是失败
        String redisKey = CommonMethod.getMiaoshaOrderWaitFlagRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
        if (!redisClient.setnx(redisKey, goodsId, 120)) {
            throw new ServerExceptionForbidden("排队中");
        }

        // 校验时间（商品不在秒杀时间范围)
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodId(goodsId);
        if (goodsVo == null) {
            redisClient.delete(redisKey);
            throw new ServerExceptionNotFound("商品不存在");
        }
        CustomerConstant.MiaoShaStatus status = ValidMSTime.validMiaoshaTime(goodsVo);
        if (!status.equals(CustomerConstant.MiaoShaStatus.START)) {
            redisClient.delete(redisKey);
            throw new ServerExceptionForbidden(status.getMessage());
        }

        // 是否已经秒杀过了
        MiaoShaOrderVo order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            redisClient.delete(redisKey);
            throw new ServerExceptionForbidden("已经秒杀成功");
        }

        //扣减库存 +  ZK 内存级别标识
        if (false == deductStockCache(goodsId)) {
            // 售完or出错
            redisClient.delete(redisKey);
            throw new ServerExceptionServerError("秒杀失败");
        }

        // 排队秒杀信息
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setUser(user);
        try {
            mqSender.sendMiaoshaMessage(miaoshaMessage);
        } catch (Exception e) {
            // 回滚
            redisClient.delete(redisKey);
            deductStockCache(goodsId);
        }

        return CustomerConstant.MS_ING;
    }

    /**
     * 获取秒杀结果
     *
     * @param user
     * @param goodsId
     * @return
     */
    @GetMapping("/result")
    public Long result(MiaoShaUser user, @RequestParam() Long goodsId) {
        if (user == null) {
            throw new ServerExceptionForbidden("用户没有登录");
        }

        // 判断redis排队, 如果不为空返回排队中
        String redisKey = CommonMethod.getMiaoshaOrderWaitFlagRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
        if (null != redisClient.get(redisKey, Long.class)) {
            throw new ServerExceptionForbidden("排队中");
        }

        redisKey = CommonMethod.getMiaoshaOrderRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
        OrderInfoVo order = redisClient.get(redisKey, OrderInfoVo.class);
        if (null == order) {
            throw new ServerExceptionNotFound("订单没有找到");
        }

        return order.getId();
    }

    //扣减库存 +  ZK + 内存级别标识
    private boolean deductStockCache(Long goodsId) {
        try {
            //检测是否准备好数据（preheat方法）
            Long stock = redisClient.decr(GoodsKeyPrefix.getMiaoshaGoodsStock, String.valueOf(goodsId));
            if (null == stock) {
                log.error("秒伤商品还没有准备好");
                return false;
            }

            // 因为秒杀库存已经为0了，再decr变为-1，所以要标记为售完
            if (stock < 0) {
                redisClient.incr(GoodsKeyPrefix.getMiaoshaGoodsStock, String.valueOf(goodsId));

                // 内存标记售完
                ProductSoutOutMap.markSoldOut(goodsId);

                // 写zk的商品售完标记
                // 1.根节点不存在创建
                if (zooKeeper.exists(CustomerConstant.ZookeeperPathPrefix.PRODUCT_SOLD_OUT, false) == null) {
                    zooKeeper.createNode(CustomerConstant.ZookeeperPathPrefix.PRODUCT_SOLD_OUT, "");
                }
                // 2.goodsId子节点不存在创建
                String zkGoodsIdPath = CustomerConstant.ZookeeperPathPrefix.getZKSoldOutProductPath(goodsId);
                if (zooKeeper.exists(zkGoodsIdPath, true) == null) {
                    zooKeeper.createNode(zkGoodsIdPath, "true");
                }
                // 2.1已存在判断
                if ("false".equals(zooKeeper.getData(zkGoodsIdPath, productSoldOutWatcher))) {
                    zooKeeper.updateNode(zkGoodsIdPath, "true");
                }
                zooKeeper.exists(zkGoodsIdPath, true);
            }
            return true;
        } catch (Exception e) {
            log.error("deduct", e);
            return false;
        }
    }

    private boolean deductStockCacheRollback(Long goodsId) {
        Long stock = redisClient.incr(GoodsKeyPrefix.getMiaoshaGoodsStock, String.valueOf(goodsId));

        // 标记库存为空
        if (stock < 0) {
            ProductSoutOutMap.clearSoldOut(goodsId);
        }

        return true;
    }
}

