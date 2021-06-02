package com.hoya.service.commons.redis.impl;

import com.hoya.service.commons.redis.BasePrefix;

public class MiaoshaKeyPrefix extends BasePrefix {

    private MiaoshaKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKeyPrefix isGoodsOver = new MiaoshaKeyPrefix(0, "go");

    public static MiaoshaKeyPrefix getMiaoshaPath = new MiaoshaKeyPrefix(60, "mp");

    public static MiaoshaKeyPrefix getMiaoshaVerifyCode = new MiaoshaKeyPrefix(300, "vc");

    public static MiaoshaKeyPrefix getMiaoshaVerifyCodeRegister = new MiaoshaKeyPrefix(300, "register");

}
