package org.example.demoproductservice.common.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.example.demoproductservice.interfaces.exception.CommonHttpException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

import static org.example.demoproductservice.interfaces.consts.ErrorCode.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CommonHttpException.class})
    public ResponseEntity<ApiResponse<Void>> handleCommonHttpException(CommonHttpException exception) {
        final ApiResponse<Void> body = ApiResponse.ofErrorCode(exception.getErrorCode());
        final HttpStatus httpStatus = exception.getHttpStatus();
        return ResponseEntity.status(httpStatus).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) 도메인 엔티티의 타입 불일치(Long 타입 파라미터에 String 타입으로 요청한 경우), @Positive
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException exception) {
        String defaultMessage = exception.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(""));
        final ApiResponse<Void> body = ApiResponse.ofErrorCodeWithMessage(INVALID_INPUT, defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) @NotBlank, @NotEmpty, @Size
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String defaultMessage = exception.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(""));
        final ApiResponse<Void> body = ApiResponse.ofErrorCodeWithMessage(INVALID_INPUT, defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) @Min, @Max, @Digits
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        final ApiResponse<Void> body = ApiResponse.ofErrorCodeWithMessage(INVALID_INPUT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) 존재하지 않는 메서드 요청
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        final ApiResponse<Void> body = ApiResponse.ofErrorCode(METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) NullPointerException
     */
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException exception) {
        final ApiResponse<Void> body = ApiResponse.ofErrorCode(INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
