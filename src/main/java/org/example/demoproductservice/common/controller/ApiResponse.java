package org.example.demoproductservice.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.demoproductservice.interfaces.consts.ErrorCode;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public record ApiResponse<T>(T data, ApiErrorResponse error) {

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> ofErrorCode(ErrorCode errorCode) {
        final ApiErrorResponse errorResponse = new ApiErrorResponse(errorCode.message, errorCode.code);
        return new ApiResponse<>(null, errorResponse);
    }

    public static <T> ApiResponse<T> ofErrorCodeWithMessage(ErrorCode errorCode, String message) {
        final ApiErrorResponse errorResponse = new ApiErrorResponse(errorCode.message + " " + message, errorCode.code);
        return new ApiResponse<>(null, errorResponse);
    }

}
