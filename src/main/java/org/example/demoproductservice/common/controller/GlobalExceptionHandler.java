package org.example.demoproductservice.common.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.example.demoproductservice.interfaces.exception.CommonHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

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
     * ex) PathVariable 변수 validation 실패
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ConstraintViolation<?>> list = exception.getConstraintViolations().stream().toList();
        ConstraintViolation<?> constraintViolation = list.get(list.size() - 1);
        final String defaultMessage = constraintViolation.getMessage();
        final ApiResponse<Void> body = ApiResponse.ofErrorCodeWithMessage(INVALID_INPUT, defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) RequestBody validation 실패
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        FieldError fieldError = fieldErrors.get(fieldErrors.size() - 1);
        final String defaultMessage = fieldError.getDefaultMessage();
        final ApiResponse<Void> body = ApiResponse.ofErrorCodeWithMessage(INVALID_INPUT, defaultMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
    }

    /**
     * ex) PathVariable 변수 타입 불일치, 파싱 에러 등
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(Exception exception) {
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
     * ex) JSON parse error (RequestBody 필드 타입 에러 등)
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        final ApiResponse<Void> body = ApiResponse.ofErrorCode(INVALID_INPUT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(body);
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
