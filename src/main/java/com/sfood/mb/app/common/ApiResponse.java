package com.sfood.mb.app.common;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
    boolean success,
    T data,
    String message,
    String errorCode,
    OffsetDateTime timestamp
) {
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, data, message, null, OffsetDateTime.now());
    }

    public static ApiResponse<Void> ok(String message) {
        return new ApiResponse<>(true, null, message, null, OffsetDateTime.now());
    }

    public static ApiResponse<Void> error(String errorCode, String message) {
        return new ApiResponse<>(false, null, message, errorCode, OffsetDateTime.now());
    }
}
