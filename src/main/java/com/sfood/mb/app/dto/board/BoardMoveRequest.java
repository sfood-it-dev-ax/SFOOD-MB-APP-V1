package com.sfood.mb.app.dto.board;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BoardMoveRequest(
        String parentBoardId,
        @NotNull(message = "sortOrder is required")
        @Min(value = 0, message = "sortOrder must be >= 0") Integer sortOrder
) {
}
