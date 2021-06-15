package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.core.exception.ServerExceptionNotFound;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.OrderService;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.OrderDetailVo;
import com.hoya.service.vo.OrderInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;

    @GetMapping("/detail")
    @ResponseBody
    public OrderDetailVo info(MiaoShaUser user, @RequestParam("orderId") Long orderId) {
        if (user == null) {
            throw new ServerExceptionForbidden("用户没有登陆");
        }

        OrderInfoVo order = orderService.getOrderById(orderId);
        if (null == order) {
            throw new ServerExceptionNotFound("订单未找到");
        }

        Long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodId(goodsId);
        if (null == goods) {
            throw new ServerExceptionNotFound("没有对应的商品");
        }

        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return vo;
    }

}
