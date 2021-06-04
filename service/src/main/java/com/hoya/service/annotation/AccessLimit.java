package com.hoya.service.annotation;

/**
 * 访问控制注解，在需要限制用户频繁刷新的接口使用
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {

    // 过期时间
    int seconds();

    // 过期时间内最多允许刷新的次数
    int maxCount();

    // 是否要登陆
    boolean needLogin() default true;
}
