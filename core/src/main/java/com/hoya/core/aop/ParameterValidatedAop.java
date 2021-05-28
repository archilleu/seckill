package com.hoya.core.aop;

import com.hoya.core.utils.RequestParametersCheck;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

@Aspect
@Configuration
public class ParameterValidatedAop {

    @Pointcut("@annotation(com.hoya.core.aop.annotation.ParameterValidated)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object logger(ProceedingJoinPoint point) throws Throwable {
        List<Object> list = Arrays.asList(point.getArgs());
        BindingResult bindingResult =  (BindingResult) list.stream().filter(param ->{
            return param instanceof BindingResult;
        }).findFirst().orElse(null);

        RequestParametersCheck.check(bindingResult);

        Object result = point.proceed();


        return result;
    }

}
