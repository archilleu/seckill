package com.hoya.service.commons.redis;

public interface RedisService {

    void put(String key, Object value, long seconds);

    Object get(String key);
}
