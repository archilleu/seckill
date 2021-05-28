package com.hoya.core.controller;

/**
 * {@link EnableGlobalExceptionHandle}返回包装器测试
 */

import com.hoya.core.exception.ResultCode;
import com.hoya.core.exception.ServerException;
import com.hoya.core.utils.RequestParametersCheck;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class EnableGlobalExceptionController {
    @GetMapping("/server-exception")
    public void serverException() {
        throw new ServerException(ResultCode.BAD_REQUEST, "bad request");
    }

    @PostMapping("/http-message-not-readable-exception")
    public void httpMessageNotReadableException(@RequestBody @Validated TestModel testModel) {
    }

    @PostMapping("/illegal-argument-exception")
    public void illegalArgumentException(@RequestBody @Validated TestModel testModel, BindingResult bindingResult) {
        RequestParametersCheck.check(bindingResult);
    }

    @PostMapping("/illegal-argument-exception1")
    public void illegalArgumentException1(@RequestParam String must) {
        return;
    }

    @PostMapping("/error-http-method-exception")
    public void errorHttpMethodException() throws HttpRequestMethodNotSupportedException {
    }

    @PostMapping("/database-exception")
    public void databaseException() throws SQLException {
        throw new SQLException("database error");
    }

}
