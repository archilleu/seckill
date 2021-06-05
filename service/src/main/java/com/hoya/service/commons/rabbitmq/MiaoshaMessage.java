package com.hoya.service.commons.rabbitmq;

import com.hoya.service.model.MiaoShaUser;
import lombok.Data;

@Data
public class MiaoshaMessage {

    private MiaoShaUser user;

    private long goodsId;
}
