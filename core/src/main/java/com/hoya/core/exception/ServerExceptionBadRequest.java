package com.hoya.core.exception;

public class ServerExceptionBadRequest extends ServerException {
    public ServerExceptionBadRequest() {
        this(null);
    }

    public ServerExceptionBadRequest(String message) {
        super(ResultCode.BAD_REQUEST, message);
    }
}
