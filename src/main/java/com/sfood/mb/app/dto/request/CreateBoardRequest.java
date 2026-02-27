package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBoardRequest(
        @NotBlank String boardId,
        String parentBoardId,
        @NotBlank @Size(max = 20) String boardName,
        @NotNull @Max(Integer.MAX_VALUE) Integer sortOrder
) {
}
