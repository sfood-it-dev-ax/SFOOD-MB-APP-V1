package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.UserResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    UserResponse login(LoginRequest request, HttpSession session);

    void logout(HttpSession session);

    UserResponse me(HttpSession session);

    String getCurrentUserId(HttpSession session);
}
