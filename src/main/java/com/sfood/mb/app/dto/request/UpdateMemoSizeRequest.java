package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemoSizeRequest(
    @NotNull(message = "width는 필수입니다.")
    Double width,

    @NotNull(message = "height는 필수입니다.")
    Double height
) {
}
