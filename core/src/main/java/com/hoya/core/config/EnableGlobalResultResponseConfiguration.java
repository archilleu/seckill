package com.hoya.core.config;

import com.hoya.core.advice.GlobalResultResponseAdvice;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 生成配置需要的bean
 */
@Configuration
@EnableConfigurationProperties(GlobalDefaultProperties.class)
@PropertySource(value = "classpath:dispose.yml", ignoreResourceNotFound = true, encoding = "UTF-8")
public class EnableGlobalResultResponseConfiguration {

    @Bean
    public GlobalResultResponseAdvice commonResponseDataAdvice(GlobalDefaultProperties globalDefaultProperties) {
        return new GlobalResultResponseAdvice(globalDefaultProperties);
    }

}
