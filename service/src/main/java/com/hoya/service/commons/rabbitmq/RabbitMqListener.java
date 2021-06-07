package com.hoya.service.commons.rabbitmq;

import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.GoodsKeyPrefix;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.util.CommonMethod;
import com.hoya.service.util.ProductSoutOutMap;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqListener {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisClient redisClient;

    // TODO: 如果出现异常需要重试?
    // FIXME: 如果多线程处理需要如何同步
    // Spring Boot RabbitMq 并发与限流(https://blog.csdn.net/linsongbin1/article/details/100658415)
    //（https://blog.csdn.net/chenghan_yang/article/details/104246869）
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE, concurrency="1")
    public void receive(String message) {
        log.info("receive message:" + message);

        MiaoshaMessage miaoshaMessage = RedisClient.stringToBean(message, MiaoshaMessage.class);
        MiaoShaUser user = miaoshaMessage.getUser();
        long goodsId = miaoshaMessage.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }

        // 判断是否已经秒杀到了
        MiaoShaOrderVo order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }

        // 减库存下订单写入秒杀系统
        MiaoShaUserVo userVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, userVo);
        try {
            OrderInfoVo miaoShaOrderVo = miaoshaService.miaosha(userVo, goods);
            // 秒杀成功，设置redis标志
            String msKey = CommonMethod.getMiaoshaOrderRedisKey(String.valueOf(user.getId()), String.valueOf(goodsId));
            redisClient.set(msKey, miaoShaOrderVo);
        } catch (Exception e) {
            // 秒杀失败，回滚
            redisClient.incr(GoodsKeyPrefix.getMiaoshaGoodsStock, String.valueOf(goodsId));
            ProductSoutOutMap.clearSoldOut(goodsId);
        }

    }

}
