package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.UserResponse;
import com.sfood.mb.app.service.AuthService;
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
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        log.info("API IN - POST /api/v1/auth/login (googleTokenLength={}, sessionId={})",
                request.googleToken() == null ? 0 : request.googleToken().length(),
                session.getId());
        return ApiResponse.success(authService.login(request, session), "로그인 성공");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        log.info("API IN - POST /api/v1/auth/logout (sessionId={})", session.getId());
        authService.logout(session);
        return ApiResponse.success(null, "로그아웃 성공");
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> me(HttpSession session) {
        log.info("API IN - GET /api/v1/auth/me (sessionId={})", session.getId());
        return ApiResponse.success(authService.me(session), "세션 사용자 조회 성공");
    }
}
