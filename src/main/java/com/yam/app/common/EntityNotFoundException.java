package com.yam.app.common;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends SystemException {

    public EntityNotFoundException(String format, Object... args) {
        super(format, args);
    }

    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
