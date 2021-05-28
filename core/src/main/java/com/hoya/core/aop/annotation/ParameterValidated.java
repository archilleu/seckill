package com.hoya.core.aop.annotation;


import java.lang.annotation.*;

/*
 * 记录方法日志注解
 * 检擦参数注解，参数入参必须有BindingResult bindingResult,否则{@link ParameterValidatedAOP}不起作用
   范例：
   @ParameterValidated
   function(@RequestBody @Validated Class c, BindingResult bindingResult)
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParameterValidated {
}
