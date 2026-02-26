package com.sfood.mb.app.auth.service;

import com.sfood.mb.app.auth.domain.UserAccountRepository;
import com.sfood.mb.app.auth.dto.LoginRequest;
import com.sfood.mb.app.auth.session.SessionStore;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.common.AuthException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SessionStore sessionStore;

    @AfterEach
    void tearDown() {
        sessionStore.clearAll();
    }

    @Test
    void login_newUser_createsAccountAndDefaultBoard() {
        var response = authService.login(new LoginRequest("new-user@test.com", "New User"));

        assertTrue(response.newUser());
        assertTrue(userAccountRepository.existsById("new-user@test.com"));
        assertFalse(boardRepository.findByUserEmailAndHideFalseOrderByCreatedAtAsc("new-user@test.com").isEmpty());
        assertNotNull(response.sessionToken());
    }

    @Test
    void login_existingUser_returnsNewSession() {
        authService.login(new LoginRequest("exist@test.com", "User A"));

        var second = authService.login(new LoginRequest("exist@test.com", "User A"));

        assertFalse(second.newUser());
        assertNotNull(second.sessionToken());
    }

    @Test
    void getSession_invalidToken_throwsAuthException() {
        assertThrows(AuthException.class, () -> authService.getSession("invalid-token"));
    }

    @Test
    void logout_invalidatesSession() {
        var response = authService.login(new LoginRequest("logout@test.com", "Logout User"));
        authService.logout(response.sessionToken());

        AuthException ex = assertThrows(AuthException.class, () -> authService.getSession(response.sessionToken()));
        assertEquals("SESSION_INVALID", ex.getCode());
    }
}
