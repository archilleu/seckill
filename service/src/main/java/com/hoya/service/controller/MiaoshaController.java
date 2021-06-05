package com.hoya.service.controller;

import com.hoya.core.exception.ServerExceptionForbidden;
import com.hoya.service.annotation.AccessLimit;
import com.hoya.service.commons.rabbitmq.MiaoshaMessage;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.commons.redis.impl.GoodsKeyPrefix;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.server.RabbitMqService;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    RabbitMqService rabbitMqService;

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @PostMapping("/{path}/do")
    public void doIt(MiaoShaUser user, @PathVariable("path") String path, @RequestParam long goodsId) {
        if (user == null) {
            throw new ServerExceptionForbidden("请登录");
        }

        // 验证path
        MiaoShaUserVo miaoShaUserVo = new MiaoShaUserVo();
        BeanUtils.copyProperties(user, miaoShaUserVo);
        boolean check = miaoshaService.checkPath(miaoShaUserVo, goodsId, path);
        if (!check) {
            throw new ServerExceptionForbidden("秒杀失败");
        }
        // 是否已经秒杀到
        MiaoShaOrderVo order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            throw new ServerExceptionForbidden("已经秒杀成功");
        }

        // redis减库存
        Long stock = redisClient.decr(GoodsKeyPrefix.getMiaoshaGoodsStock, Long.toString(goodsId));
        if (stock < 0) {
            throw new ServerExceptionForbidden("商品全部秒光了");
        }

        // 排队秒杀信息
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setUser(user);
        rabbitMqService.sendMiaoshaMessage(miaoshaMessage);

        return;
    }

    @GetMapping("/{path}/result")
    public void miaoshaResult(MiaoShaUser user, @PathVariable("path") String path, @RequestParam Long goodsId) {
        return;
    }
}
