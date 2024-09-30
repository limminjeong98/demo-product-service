package org.example.demoproductservice.common.controller;

import org.example.demoproductservice.interfaces.exception.CommonHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CommonHttpException.class})
    public ResponseEntity<ApiResponse<Void>> handleCommonHttpException(CommonHttpException exception) {
        final ApiResponse<Void> body = ApiResponse.of(exception.getErrorCode());
        final MediaType contentType = new MediaType("application", "json");
        final HttpStatus httpStatus = exception.getHttpStatus();

        return ResponseEntity.status(httpStatus).contentType(contentType).body(body);
    }
}
