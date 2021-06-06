package com.hoya.service.commons.rabbitmq;

import com.hoya.core.exception.ServerExceptionServerError;
import com.hoya.service.commons.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        try {
            String msg = RedisClient.beanToString(mm);
            log.info("mq发送订单信息：msg{}", msg);
            amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
        } catch (AmqpException e) {
            throw new ServerExceptionServerError("信息发送失败!");
        }
    }

}
