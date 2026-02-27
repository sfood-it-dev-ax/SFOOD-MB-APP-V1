package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemoPositionRequest(@NotNull Float posX, @NotNull Float posY) {
}
