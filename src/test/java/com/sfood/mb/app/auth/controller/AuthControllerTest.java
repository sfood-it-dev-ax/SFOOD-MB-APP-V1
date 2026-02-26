package com.sfood.mb.app.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.mb.app.auth.domain.UserAccountRepository;
import com.sfood.mb.app.auth.dto.LoginRequest;
import com.sfood.mb.app.auth.dto.LoginResponse;
import com.sfood.mb.app.auth.session.SessionStore;
import com.sfood.mb.app.board.domain.BoardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private SessionStore sessionStore;

    @BeforeEach
    void setUp() {
        userAccountRepository.deleteAll();
        boardRepository.deleteAll();
        sessionStore.clearAll();
    }

    @AfterEach
    void tearDown() {
        sessionStore.clearAll();
        boardRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    @Test
    void login_success() throws Exception {
        LoginRequest request = new LoginRequest("auth-user@test.com", "Auth User");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionToken").isNotEmpty())
                .andExpect(jsonPath("$.newUser").value(true));

        assertTrue(userAccountRepository.existsById("auth-user@test.com"));
        assertFalse(boardRepository.findByUserEmailAndHideFalseOrderByCreatedAtAsc("auth-user@test.com").isEmpty());
    }

    @Test
    void session_success_withToken() throws Exception {
        LoginResponse loginResponse = loginAndExtract("session-user@test.com", "Session User");

        mockMvc.perform(get("/api/v1/auth/session")
                        .header("X-Session-Token", loginResponse.sessionToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("session-user@test.com"));
    }

    @Test
    void session_fail_withoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/auth/session"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("SESSION_TOKEN_MISSING"));
    }

    @Test
    void logout_success() throws Exception {
        LoginResponse loginResponse = loginAndExtract("logout-user@test.com", "Logout User");

        mockMvc.perform(post("/api/v1/auth/logout")
                        .header("X-Session-Token", loginResponse.sessionToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/auth/session")
                        .header("X-Session-Token", loginResponse.sessionToken()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("SESSION_INVALID"));
    }

    private LoginResponse loginAndExtract(String email, String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, name))))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), LoginResponse.class);
    }
}
