package com.hoya.core.advice;

import com.hoya.core.annotation.EnableGlobalExceptionHandle;
import com.hoya.core.exception.ResultCode;
import com.hoya.core.exception.ServerError;
import com.hoya.core.exception.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * {@link GlobalExceptionHandlerAdvice}全局异常处理,处理 {@link EnableGlobalExceptionHandle}注解
 */

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    /**
     * 自定义的Server异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ServerError> appException(ServerException ex) {
        ServerError error = new ServerError(ex.getCode(), ex.getMessage());
        return new ResponseEntity<ServerError>(error, HttpStatus.valueOf(ex.getCode() / 10));
    }

    /**
     * 参数错误异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ServerError> exception(IllegalArgumentException ex) {
        ServerError error = new ServerError(ResultCode.BAD_REQUEST.getCode(), ex.getMessage());
        return new ResponseEntity<ServerError>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ServerError> exception(MissingServletRequestParameterException ex) {
        ServerError error = new ServerError(ResultCode.BAD_REQUEST.getCode(), ex.getMessage());
        return new ResponseEntity<ServerError>(error, HttpStatus.BAD_REQUEST);
    }


    /**
     * json格式错误异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ServerError> exception(HttpMessageNotReadableException ex) {
        ServerError error = new ServerError(ResultCode.BAD_REQUEST.getCode(), "json格式错误,序列化bean失败");
        return new ResponseEntity<ServerError>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * 捕获controller{@Validated} 请求入参异常
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerError> exception(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder sb = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            sb.append(fieldError.getField());
            sb.append(" ");
            sb.append(fieldError.getDefaultMessage());
            sb.append(";");
        }
        ServerError error = new ServerError(ResultCode.BAD_REQUEST.getCode(), sb.toString());
        return new ResponseEntity<ServerError>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * HTTP方法不支持异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ServerError> exception(HttpRequestMethodNotSupportedException ex) {
        ServerError error = new ServerError(ResultCode.METHOD_NOT_ALLOWED.getCode(), "不支持的HTTP方法");
        ResponseEntity<ServerError> entity = new ResponseEntity<ServerError>(error, HttpStatus.METHOD_NOT_ALLOWED);
        return entity;
    }

    /**
     * 数据库异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ServerError> exception(SQLException ex) {
        ServerError error = new ServerError(ResultCode.DATABASE_ERROR.getCode(), ResultCode.DATABASE_ERROR.getMessage());
        ResponseEntity<ServerError> entity = new ResponseEntity<ServerError>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        return entity;
    }
}