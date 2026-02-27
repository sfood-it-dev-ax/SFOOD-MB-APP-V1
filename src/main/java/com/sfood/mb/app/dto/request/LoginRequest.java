package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String googleToken) {
}
