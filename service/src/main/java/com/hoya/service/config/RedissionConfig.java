package com.hoya.service.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissionConfig {
    @Value("${spring.redis.host}")
    private  String host;

    @Value("${spring.redis.port}")
    private  String port;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient getRedisson() throws IOException {
        // 使用redis单机模式
        Config config = new Config();
        String address = REDIS_PREFIX + host + ":" + port;
        config.useSingleServer().setAddress(address);
        return Redisson.create(config);
    }

    private static final String REDIS_PREFIX = "redis://";
}
