package com.hoya.core.exception;

import org.springframework.http.HttpStatus;

/**
 * 错误码表
 */

public enum ResultCode {
    /**
     * 1000~1999 信息，服务器收到请求，需要请求者继续执行操作
     */

    /**
     * 2000~2999 成功,操作被成功接收并处理
     */
    OK(2000, HttpStatus.OK.toString())
    ,CREATED(2010, HttpStatus.CREATED.toString())
    ,ACCEPTED(2020, HttpStatus.ACCEPTED.toString())

    /**
     * 3000~3999 重定向,需要进一步操作以完成请求
     */
    ,MULTIPLE_CHOICES(3000, HttpStatus.MULTIPLE_CHOICES.toString())
    ,MOVED_PERMANENTLY(3010, HttpStatus.MOVED_PERMANENTLY.toString())
    ,FOUND(3020, HttpStatus.FOUND.toString())
    ,SEE_OTHER(3030, HttpStatus.SEE_OTHER.toString())
    ,NOT_MODIFIED(3040, HttpStatus.NOT_MODIFIED.toString())

    /**
     * 4000~4999 客户端错误，包含语法错误或无法完成请求
     */
    ,BAD_REQUEST(4000, HttpStatus.BAD_REQUEST.toString())
    ,UNAUTHORIZED(4010, HttpStatus.UNAUTHORIZED.toString())
    ,PAYMENT_REQUIRED(4020, HttpStatus.PAYMENT_REQUIRED.toString())
    ,FORBIDDEN(4030, HttpStatus.FORBIDDEN.toString())
    ,NOT_FOUND(4040, HttpStatus.NOT_FOUND.toString())
    ,METHOD_NOT_ALLOWED(4050, HttpStatus.METHOD_NOT_ALLOWED.toString())
    ,NOT_ACCEPTABLE(4060, HttpStatus.NOT_ACCEPTABLE.toString())
    ,REQUEST_TIMEOUT(4080, HttpStatus.REQUEST_TIMEOUT.toString())
    ,PAYLOAD_TOO_LARGE(4130, HttpStatus.PAYLOAD_TOO_LARGE.toString())

    /**
     * 5000~ 5999 服务器错误,服务器在处理请求过程中发生了错误
     */
    ,INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR.toString())
    ,NOT_IMPLEMENTED(5010, HttpStatus.NOT_IMPLEMENTED.toString())
    ,BAD_GATEWAY(5020, HttpStatus.BAD_GATEWAY.toString())
    ,SERVICE_UNAVAILABLE(5030, HttpStatus.SERVICE_UNAVAILABLE.toString())
    ,DATABASE_ERROR(5040, "database error")
    ;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
