package com.sfood.mb.app.common;

import java.time.LocalDateTime;

public record ApiResponse<T>(boolean success, T data, String message, LocalDateTime timestamp) {

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message, LocalDateTime.now());
    }
}
