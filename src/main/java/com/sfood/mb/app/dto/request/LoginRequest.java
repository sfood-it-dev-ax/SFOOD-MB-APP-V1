package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "googleAccount는 필수입니다.")
    @Email(message = "googleAccount 형식이 올바르지 않습니다.")
    String googleAccount,

    @NotBlank(message = "token은 필수입니다.")
    String token,

    String name,
    String profileImage
) {
}
