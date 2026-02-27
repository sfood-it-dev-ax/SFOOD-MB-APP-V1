package com.sfood.mb.app.common;

import java.time.LocalDateTime;

public record ErrorResponse(boolean success, String errorCode, String message, LocalDateTime timestamp) {

    public static ErrorResponse of(String errorCode, String message) {
        return new ErrorResponse(false, errorCode, message, LocalDateTime.now());
    }
}
