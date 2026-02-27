package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMemoRequest(
        @NotBlank String memoId,
        @NotBlank String typeId,
        String content,
        @NotNull Float posX,
        @NotNull Float posY,
        @NotNull Float width,
        @NotNull Float height,
        @NotNull Integer zIndex
) {
}
