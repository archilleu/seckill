package com.hoya.service.commons.redis.impl;


import com.hoya.service.commons.redis.BasePrefix;

public class MiaoShaUserKeyPrefix extends BasePrefix {

    public static final int TOKEN_EXPIRE = 360 * 2;

    public static MiaoShaUserKeyPrefix token = new MiaoShaUserKeyPrefix(TOKEN_EXPIRE, "tk");
    public static MiaoShaUserKeyPrefix getByNickName = new MiaoShaUserKeyPrefix(0, "nickName");

    public MiaoShaUserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
}