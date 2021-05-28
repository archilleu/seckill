package com.hoya.core.utils;

import com.hoya.core.exception.ServerExceptionBadRequest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * Model JSR 检查
 */
public class RequestParametersCheck {
    public static void check(BindingResult bindingResult) throws ServerExceptionBadRequest {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                sb.append(fieldError.getField());
                sb.append(" ");
                sb.append(fieldError.getDefaultMessage());
                sb.append(";");
            }

            throw new ServerExceptionBadRequest(sb.toString());
        }

        return;
    }
}
