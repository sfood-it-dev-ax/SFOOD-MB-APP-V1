package com.sfood.mb.app.auth.controller;

import com.sfood.mb.app.auth.dto.LoginRequest;
import com.sfood.mb.app.auth.dto.LoginResponse;
import com.sfood.mb.app.auth.dto.SessionResponse;
import com.sfood.mb.app.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader(AuthService.SESSION_HEADER);
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/session")
    public ResponseEntity<SessionResponse> getSession(HttpServletRequest request) {
        String token = request.getHeader(AuthService.SESSION_HEADER);
        return ResponseEntity.ok(authService.getSession(token));
    }
}
