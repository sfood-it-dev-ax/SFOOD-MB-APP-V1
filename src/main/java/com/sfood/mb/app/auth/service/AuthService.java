package com.sfood.mb.app.auth.service;

import com.sfood.mb.app.auth.domain.UserAccount;
import com.sfood.mb.app.auth.domain.UserAccountRepository;
import com.sfood.mb.app.auth.dto.LoginRequest;
import com.sfood.mb.app.auth.dto.LoginResponse;
import com.sfood.mb.app.auth.dto.SessionResponse;
import com.sfood.mb.app.auth.session.SessionInfo;
import com.sfood.mb.app.auth.session.SessionStore;
import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.common.AuthException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    public static final String SESSION_HEADER = "X-Session-Token";
    public static final String SESSION_REQUEST_ATTR = "sessionInfo";

    private final UserAccountRepository userAccountRepository;
    private final BoardRepository boardRepository;
    private final SessionStore sessionStore;

    public AuthService(
            UserAccountRepository userAccountRepository,
            BoardRepository boardRepository,
            SessionStore sessionStore
    ) {
        this.userAccountRepository = userAccountRepository;
        this.boardRepository = boardRepository;
        this.sessionStore = sessionStore;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserAccount user = userAccountRepository.findById(request.userEmail()).orElse(null);
        boolean newUser = false;

        if (user == null) {
            user = userAccountRepository.save(new UserAccount(request.userEmail(), request.userName()));
            createDefaultBoard(request.userEmail());
            newUser = true;
        } else if (!user.getUserName().equals(request.userName())) {
            user.rename(request.userName());
        }

        SessionInfo sessionInfo = sessionStore.create(user.getUserEmail(), user.getUserName());
        return new LoginResponse(
                sessionInfo.token(),
                sessionInfo.expiresAt(),
                user.getUserEmail(),
                user.getUserName(),
                newUser
        );
    }

    public SessionResponse getSession(String token) {
        SessionInfo sessionInfo = getValidatedSession(token);
        return new SessionResponse(
                sessionInfo.userEmail(),
                sessionInfo.userName(),
                sessionInfo.expiresAt()
        );
    }

    public void logout(String token) {
        getValidatedSession(token);
        sessionStore.invalidate(token);
    }

    public SessionInfo getValidatedSession(String token) {
        if (token == null || token.isBlank()) {
            throw new AuthException("SESSION_TOKEN_MISSING", "Session token is required.");
        }
        return sessionStore.findValid(token)
                .orElseThrow(() -> new AuthException("SESSION_INVALID", "Session is invalid or expired."));
    }

    private void createDefaultBoard(String userEmail) {
        String boardId = userEmail + "_default_" + UUID.randomUUID().toString().substring(0, 8);
        Board defaultBoard = new Board(boardId, userEmail, "기본 보드", null, 0);
        boardRepository.save(defaultBoard);
    }
}
