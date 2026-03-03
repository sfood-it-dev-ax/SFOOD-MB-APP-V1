package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemoPositionRequest(
    @NotNull(message = "posX는 필수입니다.")
    Double posX,

    @NotNull(message = "posY는 필수입니다.")
    Double posY
) {
}
