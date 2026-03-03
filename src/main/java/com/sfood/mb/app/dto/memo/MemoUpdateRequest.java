package com.sfood.mb.app.dto.memo;

import jakarta.validation.constraints.Min;

public record MemoUpdateRequest(
        String content,
        Integer x,
        Integer y,
        @Min(value = 1, message = "width must be >= 1") Integer width,
        @Min(value = 1, message = "height must be >= 1") Integer height,
        Integer zIndex
) {
}
