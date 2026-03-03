package com.sfood.mb.app.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sfood.mb.app.config.SessionConstants;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthTokenSessionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRejectWhenGoogleTokenDoesNotMatchExistingUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "token-user@example.com",
                                  "name": "Token User",
                                  "googleToken": "token-1"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "token-user@example.com",
                                  "name": "Token User",
                                  "googleToken": "token-2"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("INVALID_GOOGLE_TOKEN"));
    }

    @Test
    void shouldUpdateSessionOnLoginAndInvalidateOnLogout() throws Exception {
        HttpSession session = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "session-user@example.com",
                                  "name": "Session User",
                                  "googleToken": "session-token"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getRequest()
                .getSession(false);

        MockHttpSession mockSession = (MockHttpSession) session;

        Object loginUserId = mockSession.getAttribute(SessionConstants.LOGIN_USER_ID);
        Object loginUserName = mockSession.getAttribute(SessionConstants.LOGIN_USER_NAME);
        Object loginAt = mockSession.getAttribute(SessionConstants.LOGIN_AT);

        if (!"session-user@example.com".equals(loginUserId)
                || !"Session User".equals(loginUserName)
                || loginAt == null) {
            throw new AssertionError("Session attributes are not updated as expected");
        }

        mockMvc.perform(post("/api/auth/logout").session(mockSession))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").session(mockSession))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }
}
