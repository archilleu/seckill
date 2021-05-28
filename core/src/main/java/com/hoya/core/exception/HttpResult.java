package com.hoya.core.exception;

import lombok.Data;
import com.hoya.core.advice.GlobalResultResponseAdvice;

/**
 * {@link GlobalResultResponseAdvice} 全局包装返回对象
 */

@Data
public class HttpResult {

    public HttpResult() {

    }

    public HttpResult(ResultCode resultCode, Object data) {
        this.data = data;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    private Integer code;
    private String message;
    private Object data;
}
