package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateMemoContentRequest(@NotNull String content) {
}
