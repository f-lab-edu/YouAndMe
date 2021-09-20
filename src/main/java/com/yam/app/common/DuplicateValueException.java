package com.yam.app.common;

import org.springframework.http.HttpStatus;

public final class DuplicateValueException extends SystemException {

    public DuplicateValueException(String value) {
        super("These are duplicated value, (value : %s)", value);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
