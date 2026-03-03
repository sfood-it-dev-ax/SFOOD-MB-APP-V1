package com.sfood.mb.app.controller;

import com.sfood.mb.app.application.service.AuthService;
import com.sfood.mb.app.config.SessionConstants;
import com.sfood.mb.app.dto.auth.LoginRequest;
import com.sfood.mb.app.dto.auth.UserResponse;
import com.sfood.mb.app.dto.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest) {
        UserResponse user = authService.login(request);
        HttpSession oldSession = servletRequest.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession newSession = servletRequest.getSession(true);
        newSession.setAttribute(SessionConstants.LOGIN_USER_ID, user.userId());
        newSession.setAttribute(SessionConstants.LOGIN_USER_NAME, user.name());
        newSession.setAttribute(SessionConstants.LOGIN_AT, Instant.now().toString());
        return ApiResponse.ok(user);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        SessionAuthSupport.getRequiredUserId(session);
        session.invalidate();
        return ApiResponse.ok(null);
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(authService.getUser(userId));
    }
}
