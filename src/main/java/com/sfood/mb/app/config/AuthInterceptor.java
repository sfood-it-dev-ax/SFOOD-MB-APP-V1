package com.sfood.mb.app.config;

import com.sfood.mb.app.session.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        if ("/api/v1/auth/login".equals(path)) {
            log.info("AUTH allow - method={}, path={}", request.getMethod(), path);
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.SESSION_USER_ID) == null) {
            log.info("AUTH block (401) - method={}, path={}", request.getMethod(), path);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"errorCode\":\"UNAUTHORIZED\",\"message\":\"인증이 필요합니다.\"}");
            return false;
        }
        log.info("AUTH pass - method={}, path={}, userId={}", request.getMethod(), path, session.getAttribute(SessionConstants.SESSION_USER_ID));
        return true;
    }
}
