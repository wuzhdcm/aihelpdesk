package com.example.aihelpdesk.common;

public record Result<T>(String code, String message, T data) {

    public static <T> Result<T> success(T data) {
        return new Result<>("SUCCESS", "ok", data);
    }

    public static <T> Result<T> failure(String message) {
        return new Result<>("FAILURE", message, null);
    }


}
