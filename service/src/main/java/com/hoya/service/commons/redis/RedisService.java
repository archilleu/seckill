package com.hoya.service.commons.redis;

public interface RedisService {

    void put(String key, Object value, long seconds);

    Object get(String key);

    boolean existsKey(String key);

    void renaeKey(String oldKey, String newKey);

    void deleteKey(String key);

    void persistKey(String key);

    void setByRedission(String key);

    void lock(String lockKey, int leaseTime);
}
