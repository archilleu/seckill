package com.hoya.service.annotation;

/**
 * 记录方法日志注解
 */

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperatorLogger {
    String value() default "";
}
