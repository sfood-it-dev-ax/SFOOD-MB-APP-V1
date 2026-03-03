package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoveBoardRequest(
    String parentBoardId,

    @NotNull(message = "sortOrder는 필수입니다.")
    Integer sortOrder
) {
}
