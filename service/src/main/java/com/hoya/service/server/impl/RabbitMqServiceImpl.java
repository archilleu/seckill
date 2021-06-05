package com.hoya.service.server.impl;

import com.hoya.service.commons.rabbitmq.MQConfig;
import com.hoya.service.commons.rabbitmq.MiaoshaMessage;
import com.hoya.service.commons.redis.RedisClient;
import com.hoya.service.model.MiaoShaUser;
import com.hoya.service.server.GoodsService;
import com.hoya.service.server.MiaoshaService;
import com.hoya.service.server.OrderService;
import com.hoya.service.server.RabbitMqService;
import com.hoya.service.vo.GoodsVo;
import com.hoya.service.vo.MiaoShaOrderVo;
import com.hoya.service.vo.MiaoShaUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Override
    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
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
        miaoshaService.miaosha(userVo, goods);
    }

    @Override
    public void sendMiaoshaMessage(MiaoshaMessage message) {
        String msg = RedisClient.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }
}
