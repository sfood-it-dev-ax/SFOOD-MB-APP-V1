package com.sfood.mb.app.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApiFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullApiFlowShouldWork() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));

        HttpSession session = login();

        mockMvc.perform(get("/api/auth/me").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value("user-1"));

        mockMvc.perform(get("/api/memo-types").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3));

        mockMvc.perform(post("/api/boards")
                        .session((MockHttpSession) session)
                        .contentType("application/json")
                        .content("""
                                {
                                  "boardId": "board-memo",
                                  "name": "Memo Board"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardId").value("board-memo"));

        mockMvc.perform(get("/api/boards").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));

        mockMvc.perform(patch("/api/boards/{boardId}/name", "board-memo")
                        .session((MockHttpSession) session)
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Renamed Board"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Renamed Board"));

        mockMvc.perform(patch("/api/boards/{boardId}/move", "board-memo")
                        .session((MockHttpSession) session)
                        .contentType("application/json")
                        .content("""
                                {
                                  "parentBoardId": "user-1_default",
                                  "sortOrder": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.parentBoardId").value("user-1_default"))
                .andExpect(jsonPath("$.data.sortOrder").value(1));

        mockMvc.perform(post("/api/memos")
                        .session((MockHttpSession) session)
                        .contentType("application/json")
                        .content("""
                                {
                                  "memoId": "memo-1",
                                  "boardId": "board-memo",
                                  "memoTypeId": "basic-yellow",
                                  "content": "initial",
                                  "x": 10,
                                  "y": 20,
                                  "width": 320,
                                  "height": 240,
                                  "zIndex": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memoId").value("memo-1"));

        mockMvc.perform(get("/api/memos").session((MockHttpSession) session).param("boardId", "board-memo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));

        mockMvc.perform(patch("/api/memos/{memoId}", "memo-1")
                        .session((MockHttpSession) session)
                        .contentType("application/json")
                        .content("""
                                {
                                  "content": "updated",
                                  "x": 100,
                                  "y": 200,
                                  "width": 640,
                                  "height": 300,
                                  "zIndex": 7
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("updated"))
                .andExpect(jsonPath("$.data.x").value(100))
                .andExpect(jsonPath("$.data.y").value(200))
                .andExpect(jsonPath("$.data.width").value(640))
                .andExpect(jsonPath("$.data.height").value(300))
                .andExpect(jsonPath("$.data.zIndex").value(7));

        mockMvc.perform(delete("/api/memos/{memoId}", "memo-1").session((MockHttpSession) session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/memos").session((MockHttpSession) session).param("boardId", "board-memo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(0));

        mockMvc.perform(delete("/api/boards/{boardId}", "board-memo").session((MockHttpSession) session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/boards").session((MockHttpSession) session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));

        mockMvc.perform(post("/api/auth/logout").session((MockHttpSession) session))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/auth/me").session((MockHttpSession) session))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value("UNAUTHORIZED"));
    }

    private HttpSession login() throws Exception {
        return mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "user-1",
                                  "name": "User One",
                                  "googleToken": "token-user-1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn()
                .getRequest()
                .getSession(false);
    }
}
