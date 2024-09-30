package org.example.demoproductservice.interfaces.exception;

import org.example.demoproductservice.interfaces.consts.ErrorCode;
import org.springframework.http.HttpStatus;

public class CommonHttpException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public CommonHttpException(final ErrorCode errorCode, final HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
