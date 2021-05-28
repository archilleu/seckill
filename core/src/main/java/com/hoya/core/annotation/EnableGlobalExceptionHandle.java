package com.hoya.core.annotation;


import com.hoya.core.config.EnableGlobalExceptionHandlerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启全局返回值包装注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EnableGlobalExceptionHandlerConfiguration.class)//显示导入类，不然spring boot 默认扫描不到这个类
public @interface EnableGlobalExceptionHandle {
}
