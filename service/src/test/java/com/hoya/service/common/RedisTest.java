package com.hoya.service.common;

import com.hoya.service.BaseTests;
import com.hoya.service.commons.redis.RedisService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisTest extends BaseTests {
    @Autowired
    RedisService redisService;

    public RedisTest() {
        super(RedisTest.class);
    }

    @Override
    public void init() {

    }

    @Override
    public void done() {

    }

    @Test
    public void test() {
        redisService.put(KEY, VALUE, 2000);
        String value = (String) redisService.get(KEY);
        Assert.assertEquals(value, VALUE);
    }

    private final static String KEY = "redis-key";
    private final static String VALUE = "redis-value";
}
