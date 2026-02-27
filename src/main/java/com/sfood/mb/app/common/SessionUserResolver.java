package com.sfood.mb.app.common;

import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import com.sfood.mb.app.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class SessionUserResolver {

    public String resolve(HttpSession session) {
        Object userId = session.getAttribute(AuthServiceImpl.SESSION_USER_ID);
        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "Session is not authenticated");
        }
        return userId.toString();
    }
}
