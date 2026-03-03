package com.sfood.mb.app.infrastructure.service.impl;

import com.sfood.mb.app.application.service.AuthService;
import com.sfood.mb.app.application.service.BoardService;
import com.sfood.mb.app.domain.User;
import com.sfood.mb.app.dto.auth.LoginRequest;
import com.sfood.mb.app.dto.auth.UserResponse;
import com.sfood.mb.app.exception.AppException;
import com.sfood.mb.app.infrastructure.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BoardService boardService;

    public AuthServiceImpl(UserRepository userRepository, BoardService boardService) {
        this.userRepository = userRepository;
        this.boardService = boardService;
    }

    @Override
    @Transactional
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findById(request.userId())
                .map(found -> validateAndUpdateUser(found, request))
                .orElseGet(() -> userRepository.save(new User(request.userId(), request.name(), request.googleToken())));

        userRepository.save(user);
        boardService.createDefaultBoardIfAbsent(user.userId());
        return new UserResponse(user.userId(), user.name());
    }

    @Override
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"));
        return new UserResponse(user.userId(), user.name());
    }

    private User validateAndUpdateUser(User user, LoginRequest request) {
        user.bindGoogleTokenIfMissing(request.googleToken());
        if (!request.googleToken().equals(user.googleToken())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "INVALID_GOOGLE_TOKEN", "Invalid google token");
        }
        if (!request.name().equals(user.name())) {
            user.updateName(request.name());
        }
        return user;
    }
}
