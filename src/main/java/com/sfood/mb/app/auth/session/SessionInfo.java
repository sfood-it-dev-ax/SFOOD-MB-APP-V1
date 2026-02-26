package com.sfood.mb.app.auth.session;

import java.time.LocalDateTime;

public record SessionInfo(
        String token,
        String userEmail,
        String userName,
        LocalDateTime expiresAt
) {
}
