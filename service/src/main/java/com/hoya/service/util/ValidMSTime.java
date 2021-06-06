package com.hoya.service.util;

import com.hoya.service.constant.CustomerConstant.MiaoShaStatus;
import com.hoya.service.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidMSTime {

    public static MiaoShaStatus validMiaoshaTime(GoodsVo goodsVo) {
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        //秒杀还没开始
        if (now < startAt) {
            return MiaoShaStatus.NOT_START;
        } else if (now > endAt) {
            return MiaoShaStatus.END;
        }

        return MiaoShaStatus.START;
    }

}
