package com.sfood.mb.app.auth.session;

import com.sfood.mb.app.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public SessionAuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(AuthService.SESSION_HEADER);
        SessionInfo sessionInfo = authService.getValidatedSession(token);
        request.setAttribute(AuthService.SESSION_REQUEST_ATTR, sessionInfo);
        return true;
    }
}
