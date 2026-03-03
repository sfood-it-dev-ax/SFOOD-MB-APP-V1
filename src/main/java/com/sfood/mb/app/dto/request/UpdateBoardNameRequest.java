package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBoardNameRequest(
    @NotBlank(message = "boardName은 필수입니다.")
    @Size(max = 20, message = "boardName은 20자를 초과할 수 없습니다.")
    String boardName
) {
}
