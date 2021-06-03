package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/do")
    public MiaoshaResultVo doIt(MiaoShaUser user, @RequestParam long goodsId) {
        if (user == null) {
            throw new ServerExceptionForbidden("请登录");
        }

        // 判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            throw new ServerExceptionForbidden("秒杀失败");
        }

        // 已经存在订单了，不能再次秒杀
        MiaoShaOrderVo order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            throw new ServerExceptionForbidden("秒杀失败");
        }

        // 减少库存，下订单，写入秒杀订单
        MiaoShaUserVo userVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, userVo);

        OrderInfoVo orderInfo = miaoshaService.miaosha(userVo, goods);
        MiaoshaResultVo miaoshaResultVo = new MiaoshaResultVo();
        miaoshaResultVo.setOrderInfoVo(orderInfo);
        miaoshaResultVo.setGoodsVo(goods);
        return miaoshaResultVo;
    }
}
