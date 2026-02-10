package com.example.hrmsbackend.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Integer statusCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}