package com.hoya.core.advice;

import com.alibaba.fastjson.JSON;
import com.hoya.core.config.GlobalDefaultProperties;
import com.hoya.core.annotation.EnableGlobalResultResponse;
import com.hoya.core.annotation.IgnoreGlobalResultDispose;
import com.hoya.core.exception.HttpResult;
import com.hoya.core.exception.ResultCode;
import com.hoya.core.exception.ServerError;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * {@link EnableGlobalResultResponse} 处理解析 {@link GlobalResultResponseAdvice}数据包装器
 */

@RestControllerAdvice
public class GlobalResultResponseAdvice implements ResponseBodyAdvice<Object> {

    final private GlobalDefaultProperties globalDefaultProperties;

    public GlobalResultResponseAdvice(GlobalDefaultProperties globalDefaultProperties) {
        this.globalDefaultProperties = globalDefaultProperties;
    }

    @Override
    @SuppressWarnings("all")
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return filter(methodParameter);
    }

    @Nullable
    @Override
    @SuppressWarnings("all")
    public Object beforeBodyWrite(Object resp, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if(resp instanceof HttpResult) {
            return resp;
        }

        if(resp instanceof ServerError) {
            return resp;
        }
        /**
         * string 特殊处理
         */
         if (resp instanceof String) {
             return JSON.toJSON(new HttpResult(ResultCode.OK, resp)).toString();
         }

        HttpResult result = new HttpResult(ResultCode.OK, resp);
        return result;
    }

    private Boolean filter(MethodParameter methodParameter) {
        // 检查注解是否存在
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreGlobalResultDispose.class)) {
            return false;
        }
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreGlobalResultDispose.class)) {
            return false;
        }

        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 检查过滤包路径
        long count = globalDefaultProperties.getAdvicePackage().stream()
                .filter(l -> declaringClass.getName().contains(l)).count();
        if (count > 0) {
            return true;
        }

        // 检查<类>过滤列表
        if (globalDefaultProperties.getAdviceClass().contains(declaringClass.getName())) {
            return true;
        }


        return false;
    }
}
