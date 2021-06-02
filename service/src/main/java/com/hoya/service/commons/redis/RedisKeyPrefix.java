package com.hoya.service.commons.redis;

public interface RedisKeyPrefix {

    long expireSeconds();

    String getPrefix();
}
