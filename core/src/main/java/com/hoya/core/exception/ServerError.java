package com.hoya.core.exception;

import lombok.Getter;
import  com.hoya.core.advice.GlobalExceptionHandlerAdvice;

/**
 * REST API 错误对象,供{@link GlobalExceptionHandlerAdvice} 使用
 */

@Getter
public class ServerError {
    public ServerError(int status, String message) {
        this.code = status;
        this.message = message;
    }

    public ServerError(int status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    private int code;
    private String message;
    private Object data;
}
