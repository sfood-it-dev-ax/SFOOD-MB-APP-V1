package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBoardRequest(
    @NotBlank(message = "boardId는 필수입니다.")
    String boardId,

    String parentBoardId,

    @NotBlank(message = "boardName은 필수입니다.")
    @Size(max = 20, message = "boardName은 20자를 초과할 수 없습니다.")
    String boardName,

    @NotNull(message = "sortOrder는 필수입니다.")
    Integer sortOrder
) {
}
