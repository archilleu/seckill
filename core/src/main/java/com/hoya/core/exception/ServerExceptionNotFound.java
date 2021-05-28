package com.hoya.core.exception;

public class ServerExceptionNotFound extends ServerException {
    public ServerExceptionNotFound() {
        this(null);
    }

    public ServerExceptionNotFound(String message) {
        super(ResultCode.NOT_FOUND, message);
    }
}
