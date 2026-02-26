package com.sfood.mb.app.auth.dto;

import java.time.LocalDateTime;

public record SessionResponse(
        String userEmail,
        String userName,
        LocalDateTime expiresAt
) {
}
