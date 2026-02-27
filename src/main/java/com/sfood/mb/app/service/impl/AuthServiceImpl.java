package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.domain.GoogleToken;
import com.sfood.mb.app.domain.User;
import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.UserResponse;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.repository.GoogleTokenRepository;
import com.sfood.mb.app.repository.UserRepository;
import com.sfood.mb.app.service.auth.GoogleTokenVerifier;
import com.sfood.mb.app.service.auth.VerifiedGoogleUser;
import com.sfood.mb.app.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    public static final String SESSION_USER_ID = "LOGIN_USER_ID";

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final GoogleTokenRepository googleTokenRepository;
    private final GoogleTokenVerifier googleTokenVerifier;

    public AuthServiceImpl(
            UserRepository userRepository,
            BoardRepository boardRepository,
            GoogleTokenRepository googleTokenRepository,
            GoogleTokenVerifier googleTokenVerifier
    ) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
        this.googleTokenRepository = googleTokenRepository;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Override
    public UserResponse login(LoginRequest request, HttpSession session) {
        VerifiedGoogleUser verifiedUser = googleTokenVerifier.verify(request.googleToken());
        User user = userRepository.findById(verifiedUser.userId())
                .orElseGet(() -> createUserAndDefaultBoard(verifiedUser));
        validateAndPersistGoogleToken(user.getUserId(), request.googleToken());

        session.setAttribute(SESSION_USER_ID, user.getUserId());
        session.setMaxInactiveInterval(24 * 60 * 60);

        return new UserResponse(user.getUserId(), user.getName(), user.getProfileImage());
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public UserResponse me(HttpSession session) {
        String userId = getCurrentUserId(session);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "User not found"));
        return new UserResponse(user.getUserId(), user.getName(), user.getProfileImage());
    }

    @Override
    public String getCurrentUserId(HttpSession session) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "Session is not authenticated");
        }
        return userId.toString();
    }

    private User createUserAndDefaultBoard(VerifiedGoogleUser verifiedUser) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                verifiedUser.userId(),
                verifiedUser.userId(),
                verifiedUser.name(),
                verifiedUser.profileImage(),
                now,
                now,
                false
        );
        userRepository.save(user);

        Board defaultBoard = new Board(
                verifiedUser.userId() + "_default",
                verifiedUser.userId(),
                null,
                "기본 보드",
                1,
                now,
                now,
                false
        );
        boardRepository.save(defaultBoard);

        return user;
    }

    private void validateAndPersistGoogleToken(String userId, String googleTokenValue) {
        GoogleToken googleToken = googleTokenRepository.findByUserId(userId)
                .orElseGet(() -> {
                    LocalDateTime now = LocalDateTime.now();
                    return new GoogleToken(userId, googleTokenValue, now, now);
                });

        if (!googleToken.getGoogleToken().equals(googleTokenValue)) {
            throw new ApiException(ErrorCode.UNAUTHORIZED, HttpStatus.UNAUTHORIZED, "Google token mismatch");
        }
        googleToken.updateToken(googleTokenValue);
        googleTokenRepository.save(googleToken);
    }
}
