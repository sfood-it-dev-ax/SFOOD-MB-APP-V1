package com.sfood.mb.app.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "userId is required") String userId,
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "googleToken is required") String googleToken
) {
}
