package com.sfood.mb.app.dto.common;

import java.time.Instant;

public record ApiError(String code, String message, String path, Instant timestamp) {
}
