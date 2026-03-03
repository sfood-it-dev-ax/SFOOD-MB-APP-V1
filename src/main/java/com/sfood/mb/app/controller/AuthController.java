package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.AuthMeResponse;
import com.sfood.mb.app.service.AuthService;
import com.sfood.mb.app.session.SessionConstants;
import com.sfood.mb.app.session.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthMeResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        log.info("API /auth/login request - googleAccount={}", request.googleAccount());
        AuthMeResponse response = authService.login(request, session);
        log.info("API /auth/login success - userId={}", response.userId());
        return ApiResponse.ok(response, "로그인 성공");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        String userId = session == null ? null : (String) session.getAttribute(SessionConstants.SESSION_USER_ID);
        log.info("API /auth/logout request - userId={}", userId);
        authService.logout(session);
        log.info("API /auth/logout success - userId={}", userId);
        return ApiResponse.ok("로그아웃 성공");
    }

    @GetMapping("/me")
    public ApiResponse<AuthMeResponse> me(HttpSession session) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /auth/me request - userId={}", userId);
        AuthMeResponse response = authService.me(userId);
        log.info("API /auth/me success - userId={}", response.userId());
        return ApiResponse.ok(response, "조회 성공");
    }
}
