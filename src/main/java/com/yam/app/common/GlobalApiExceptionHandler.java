package com.yam.app.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public final class GlobalApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ApiResult<?>> handleMethod(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        return ResponseEntity
            .badRequest()
            .body(ApiResult.error("Invalid argument"));
    }

    @ExceptionHandler(BindException.class)
    private ResponseEntity<ApiResult<?>> handleBind(BindException e) {
        log.error("BindException", e);
        return ResponseEntity
            .badRequest()
            .body(ApiResult.error("Invalid argument"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    private ResponseEntity<ApiResult<?>> handleNotSupported(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException", e);
        return ResponseEntity
            .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .body(ApiResult.error("Http media type not supported"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private ResponseEntity<ApiResult<?>> handleNotSupported(
        HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return ResponseEntity
            .status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ApiResult.error("Http request method not supported"));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ApiResult<?>> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity
            .internalServerError()
            .body(ApiResult.error("Internal server error"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ApiResult<?>> handleNotFound(EntityNotFoundException e) {
        log.error("EntityNotFoundException", e);
        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResult.error("Not found resources"));
    }

    @ExceptionHandler(DuplicateValueException.class)
    private ResponseEntity<ApiResult<?>> handleDuplicate(DuplicateValueException e) {
        log.error("DuplicateValueException", e);
        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResult.error("Duplicated value"));
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<ApiResult<?>> handleIllegalState(IllegalStateException e) {
        log.error("IllegalStateException", e);
        return ResponseEntity
            .badRequest()
            .body(ApiResult.error(e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedRequestException.class)
    private ResponseEntity<ApiResult<?>> handleUnauthorized(UnauthorizedRequestException e) {
        log.error("UnauthorizedRequestException", e);
        return ResponseEntity
            .status(e.getStatus())
            .body(ApiResult.error(e.getMessage()));
    }
}
