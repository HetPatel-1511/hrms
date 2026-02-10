package com.example.hrmsbackend.utils;


import com.example.hrmsbackend.dtos.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setErrors(null);
        response.setStatusCode(statusCode);
        return response;
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(List<String> errors, String message, int errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setErrors(errors);
        response.setStatusCode(errorCode);
        return ResponseEntity.status(errorCode).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String error, String message, int errorCode) {
        return error(Arrays.asList(error), message, errorCode);
    }
}
