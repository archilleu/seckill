package com.hoya.service.server.impl;

import com.hoya.service.model.OrderInfo;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderInfoService;
import com.hoya.service.server.OrderService;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import com.hoya.service.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderInfoService orderInfoService;

    @Autowired
    MiaoshaService miaoshaService;

    @Override
    public MiaoShaOrderVo getMiaoshaOrderByUserIdGoodsId(Long userId, Long goodsId) {
        return miaoshaService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
    }

    @Override
    public OrderInfoVo createOrder(MiaoShaUserVo user, GoodsVo goods) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderInfoService.insertSelective(orderInfo);
        MiaoShaOrderVo miaoshaOrder = new MiaoShaOrderVo();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        miaoshaService.insertMiaoshaOrder(miaoshaOrder);

        OrderInfoVo orderInfoVo = new OrderInfoVo();
        BeanUtils.copyProperties(orderInfo, orderInfoVo);
        return orderInfoVo;
    }

    @Override
    public OrderInfoVo getOrderById(Long orderId) {
        return null;
    }
}
