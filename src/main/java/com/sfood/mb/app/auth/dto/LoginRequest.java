package com.sfood.mb.app.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "userEmail is required")
        @Email(message = "userEmail must be a valid email")
        String userEmail,

        @NotBlank(message = "userName is required")
        @Size(max = 120, message = "userName max length is 120")
        String userName
) {
}
