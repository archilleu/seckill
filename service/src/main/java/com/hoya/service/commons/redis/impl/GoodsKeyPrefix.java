package com.hoya.service.commons.redis.impl;

import com.hoya.service.commons.redis.BasePrefix;

public class GoodsKeyPrefix extends BasePrefix {

    private GoodsKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKeyPrefix getGoodsList = new GoodsKeyPrefix(60, "gl");

    public static GoodsKeyPrefix getGoodsDetail = new GoodsKeyPrefix(60, "gd");

    public static GoodsKeyPrefix getMiaoshaGoodsStock = new GoodsKeyPrefix(0, "gs");

}
