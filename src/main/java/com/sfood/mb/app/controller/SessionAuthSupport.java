package com.sfood.mb.app.controller;

import com.sfood.mb.app.config.SessionConstants;
import com.sfood.mb.app.exception.AppException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;

public final class SessionAuthSupport {

    private SessionAuthSupport() {
    }

    public static String getRequiredUserId(HttpSession session) {
        Object value = session.getAttribute(SessionConstants.LOGIN_USER_ID);
        if (value == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Login required");
        }
        return value.toString();
    }
}
