package com.hoya.service.vo;

import com.hoya.service.model.MiaoShaUser;
import lombok.Data;

@Data
public class GoodsDetailVo {

    private int miaoshaStatus = 0;

    private int remainSeconds = 0;

    private GoodsVo goods;

    private MiaoShaUser user;
}
