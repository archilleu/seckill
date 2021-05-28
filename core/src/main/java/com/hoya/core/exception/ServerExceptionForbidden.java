package com.hoya.core.exception;

public class ServerExceptionForbidden extends ServerException {
    public ServerExceptionForbidden() {
        this(null);
    }

    public ServerExceptionForbidden(String message) {
        super(ResultCode.FORBIDDEN, message);
    }
}
