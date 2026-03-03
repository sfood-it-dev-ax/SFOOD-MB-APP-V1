package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMemoRequest(
    @NotBlank(message = "memoId는 필수입니다.")
    String memoId,

    @NotBlank(message = "typeId는 필수입니다.")
    String typeId,

    String content,

    @NotNull(message = "posX는 필수입니다.")
    Double posX,

    @NotNull(message = "posY는 필수입니다.")
    Double posY,

    @NotNull(message = "width는 필수입니다.")
    Double width,

    @NotNull(message = "height는 필수입니다.")
    Double height,

    @NotNull(message = "zIndex는 필수입니다.")
    Integer zIndex
) {
}
