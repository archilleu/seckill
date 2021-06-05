package com.hoya.service.server;

import com.hoya.service.commons.rabbitmq.MiaoshaMessage;

public interface RabbitMqService {

    public void receive(String message);

    void sendMiaoshaMessage(MiaoshaMessage message);
}
