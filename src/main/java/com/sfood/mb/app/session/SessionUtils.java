package com.sfood.mb.app.session;

import com.sfood.mb.app.exception.ApiException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

public final class SessionUtils {
    private SessionUtils() {
    }

    public static String requireUserId(HttpSession session) {
        if (session == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다.");
        }
        Object userId = session.getAttribute(SessionConstants.SESSION_USER_ID);
        if (userId instanceof String id && !id.isBlank()) {
            return id;
        }
        throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "세션이 유효하지 않습니다.");
    }
}
