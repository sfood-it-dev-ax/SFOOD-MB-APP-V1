package com.sfood.mb.app.dto.memo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemoCreateRequest(
        @NotBlank(message = "memoId is required") String memoId,
        @NotBlank(message = "boardId is required") String boardId,
        @NotBlank(message = "memoTypeId is required") String memoTypeId,
        String content,
        @NotNull(message = "x is required") Integer x,
        @NotNull(message = "y is required") Integer y,
        @NotNull(message = "width is required") @Min(value = 1, message = "width must be >= 1") Integer width,
        @NotNull(message = "height is required") @Min(value = 1, message = "height must be >= 1") Integer height,
        @NotNull(message = "zIndex is required") Integer zIndex
) {
}
