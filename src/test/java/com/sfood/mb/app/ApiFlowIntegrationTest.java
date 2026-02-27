package com.sfood.mb.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import jakarta.servlet.http.Cookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullApiFlow_shouldSucceed() throws Exception {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String boardId = "tester@example.com_board_" + uniqueSuffix;
        String memoId = boardId + "_memo1";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"googleToken":"tester@example.com|tester|https://img"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value("tester@example.com"))
                .andReturn();
        Cookie sessionCookie = updateSessionCookie(null, loginResult);

        MvcResult meResult = mockMvc.perform(get("/api/v1/auth/me").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value("tester@example.com"))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, meResult);

        MvcResult createBoardResult = mockMvc.perform(post("/api/v1/boards")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "boardId":"%s",
                                  "parentBoardId":null,
                                  "boardName":"새 보드",
                                  "sortOrder":2
                                }
                                """.formatted(boardId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardId").value(boardId))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, createBoardResult);

        MvcResult updateBoardResult = mockMvc.perform(patch("/api/v1/boards/" + boardId + "/name")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"boardName":"수정된 보드"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.boardName").value("수정된 보드"))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, updateBoardResult);

        MvcResult createMemoResult = mockMvc.perform(post("/api/v1/boards/" + boardId + "/memos")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "memoId":"%s",
                                  "typeId":"TYPE_BASIC",
                                  "content":"hello",
                                  "posX":10,
                                  "posY":20,
                                  "width":200,
                                  "height":120,
                                  "zIndex":1
                                }
                                """.formatted(memoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memoId").value(memoId))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, createMemoResult);

        MvcResult updateContentResult = mockMvc.perform(patch("/api/v1/memos/" + memoId + "/content")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"updated"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").value("updated"))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, updateContentResult);

        MvcResult zIndexResult = mockMvc.perform(patch("/api/v1/boards/" + boardId + "/memos/zindex")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memos":[{"memoId":"%s","zIndex":5}]}
                                """.formatted(memoId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, zIndexResult);

        MvcResult memoTypesResult = mockMvc.perform(get("/api/v1/memo-types").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].typeId").exists())
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, memoTypesResult);

        MvcResult deleteMemoResult = mockMvc.perform(delete("/api/v1/memos/" + memoId).cookie(sessionCookie))
                .andExpect(status().isOk())
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, deleteMemoResult);

        MvcResult deleteBoardResult = mockMvc.perform(delete("/api/v1/boards/" + boardId).cookie(sessionCookie))
                .andExpect(status().isOk())
                .andReturn();
        sessionCookie = updateSessionCookie(sessionCookie, deleteBoardResult);

        mockMvc.perform(post("/api/v1/auth/logout").cookie(sessionCookie))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    private Cookie updateSessionCookie(Cookie current, MvcResult result) {
        Cookie updated = result.getResponse().getCookie("SESSION");
        return updated != null ? updated : current;
    }
}
