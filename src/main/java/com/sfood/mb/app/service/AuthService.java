package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.AuthMeResponse;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    AuthMeResponse login(LoginRequest request, HttpSession session);

    void logout(HttpSession session);

    AuthMeResponse me(String userId);
}
