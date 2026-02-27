package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemoSizeRequest(@NotNull Float width, @NotNull Float height) {
}
