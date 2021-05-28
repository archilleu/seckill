package com.hoya.core.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * dispose配置文件
 */

@Data
@ConfigurationProperties(GlobalDefaultProperties.PREFIX)
public class GlobalDefaultProperties {

    public static final String PREFIX = "dispose";

    /**
     * 统一返回过滤包
     * 要忽略的包
     */
    @Value("${advice-package:[]}")
    private List<String> advicePackage = new ArrayList<>();

    /**
     * 统一返回过滤类
     * 要忽略的类
     */
    @Value("${advice-class:[]}")
    private List<String> adviceClass = new ArrayList<>();
}
