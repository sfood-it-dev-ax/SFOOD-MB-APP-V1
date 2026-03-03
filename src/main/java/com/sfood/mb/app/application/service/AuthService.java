package com.sfood.mb.app.application.service;

import com.sfood.mb.app.dto.auth.LoginRequest;
import com.sfood.mb.app.dto.auth.UserResponse;

public interface AuthService {

    UserResponse login(LoginRequest request);

    UserResponse getUser(String userId);
}
