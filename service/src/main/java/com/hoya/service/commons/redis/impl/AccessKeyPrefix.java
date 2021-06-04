package com.hoya.service.commons.redis.impl;

import com.hoya.service.commons.redis.BasePrefix;

public class AccessKeyPrefix extends BasePrefix {

    private AccessKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKeyPrefix withExpire(int expireSeconds) {
        return new AccessKeyPrefix(expireSeconds, "access");
    }
}
