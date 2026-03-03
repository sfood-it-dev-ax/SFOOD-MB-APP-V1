package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.domain.User;
import com.sfood.mb.app.dto.request.LoginRequest;
import com.sfood.mb.app.dto.response.AuthMeResponse;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.repository.UserRepository;
import com.sfood.mb.app.service.AuthService;
import com.sfood.mb.app.session.SessionConstants;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public AuthServiceImpl(UserRepository userRepository, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public AuthMeResponse login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUserIdAndHideFalse(request.googleAccount())
            .orElseGet(() -> createNewUser(request));

        session.setAttribute(SessionConstants.SESSION_USER_ID, user.getUserId());
        return new AuthMeResponse(user.getUserId(), user.getName(), user.getProfileImage());
    }

    private User createNewUser(LoginRequest request) {
        String displayName = request.name() == null || request.name().isBlank() ? "New User" : request.name();
        User created = userRepository.save(User.create(request.googleAccount(), displayName, request.profileImage()));
        String defaultBoardId = request.googleAccount() + "_default";
        boardRepository.findByBoardIdAndHideFalse(defaultBoardId)
            .orElseGet(() -> boardRepository.save(Board.create(defaultBoardId, created.getUserId(), null, "기본 보드", 0)));
        return created;
    }

    @Override
    public void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthMeResponse me(String userId) {
        User user = userRepository.findByUserIdAndHideFalse(userId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "사용자를 찾을 수 없습니다."));
        return new AuthMeResponse(user.getUserId(), user.getName(), user.getProfileImage());
    }
}
