package com.example.eventmanagementsystem.response;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponse<T> {
    public boolean isSuccess;
    public T data;
    public List<String> errors;
    public int statusCode;

    public void Success(T data, int statusCode) {
        isSuccess = true;
        this.data = data;
        errors = null;
        this.statusCode = statusCode;
    }

    public void Failure(List<String> errors, int statusCode) {
        isSuccess = false;
        data = null;
        this.errors = errors;
        this.statusCode = statusCode;
    }

    public void Failure(String error, int statusCode) {
        isSuccess = false;
        data = null;
        errors = List.of(error);
    }
}
