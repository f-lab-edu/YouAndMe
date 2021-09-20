package com.yam.app.common;

import org.springframework.http.HttpStatus;

public final class UnauthorizedRequestException extends SystemException {

    public UnauthorizedRequestException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
