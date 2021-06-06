package com.hoya.service.server.impl;

import com.hoya.core.exception.ServerExceptionServerError;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.MiaoshaKeyPrefix;
import com.hoya.service.dao.MiaoShaOrderMapper;
import com.hoya.service.model.MiaoShaOrder;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.util.UUIDUtil;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class MiaoShaServiceImpl implements MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoShaOrderMapper miaoShaOrderMapper;

    @Autowired
    RedisClient redisClient;

    @Override
    @Transactional(rollbackFor=Exception.class)
    public OrderInfoVo miaosha(MiaoShaUserVo user, GoodsVo goods) {
        // 1.减库存
        goodsService.reduceStock(goods);

        // 2.生成订单
        return orderService.createOrder(user, goods);
    }

    @Override
    public Integer insertMiaoshaOrder(MiaoShaOrderVo miaoshaOrder) {
        MiaoShaOrder order = new MiaoShaOrder();
        BeanUtils.copyProperties(miaoshaOrder, order);
        return miaoShaOrderMapper.insertSelective(order);
    }

    @Override
    public MiaoShaOrderVo getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        MiaoShaOrder miaoshaOrder = miaoShaOrderMapper.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
        if (null == miaoshaOrder) {
            return null;
        }

        MiaoShaOrderVo miaoShaOrderVo = new MiaoShaOrderVo();
        BeanUtils.copyProperties(miaoshaOrder, miaoShaOrderVo);
        return miaoShaOrderVo;
    }

    @Override
    public String createMiaoshaPath(MiaoShaUserVo user, Long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }

        try {
            String str = UUIDUtil.getUUid() + "123456";
            str = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
            redisClient.set(MiaoshaKeyPrefix.getMiaoshaPath, genPathName(user.getNickname(), goodsId), str);
            return str;
        } catch (Exception e) {
            log.error("秒是啊失败:{}", e);
            throw new ServerExceptionServerError("秒杀失败");
        }
    }

    @Override
    public Long getMiaoshaResult(Long userId, Long goodsId) {
        return null;
    }

    @Override
    public Boolean checkPath(MiaoShaUserVo user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String pathOld = redisClient.get(MiaoshaKeyPrefix.getMiaoshaPath, genPathName(user.getNickname(), goodsId), String.class);
        return path.equals(pathOld);
    }

    private String genPathName(String name, long goodsId) {
        return name + "_" + goodsId;
    }
}
