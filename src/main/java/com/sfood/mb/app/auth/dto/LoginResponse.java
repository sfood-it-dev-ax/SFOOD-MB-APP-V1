package com.sfood.mb.app.auth.dto;

import java.time.LocalDateTime;

public record LoginResponse(
        String sessionToken,
        LocalDateTime expiresAt,
        String userEmail,
        String userName,
        boolean newUser
) {
}
