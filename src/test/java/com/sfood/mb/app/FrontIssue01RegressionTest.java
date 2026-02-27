package com.sfood.mb.app;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FrontIssue01RegressionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void frontReportedEndpoints_shouldNotReturnServerError() throws Exception {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String userEmail = "front." + uniqueSuffix + "@example.com";
        String boardId = "b_" + uniqueSuffix + "_e2e";
        String memoId = boardId + "_m1";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"googleToken":"%s|Front User|"}
                                """.formatted(userEmail)))
                .andExpect(status().isOk())
                .andReturn();
        Cookie sessionCookie = updateSessionCookie(null, loginResult);

        MvcResult createBoardResult = ensureNot5xx(
                mockMvc.perform(post("/api/v1/boards")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "boardId":"%s",
                                  "parentBoardId":null,
                                  "boardName":"FrontE2E",
                                  "sortOrder":501
                                }
                                """.formatted(boardId)))
                        .andReturn(),
                "POST /api/v1/boards"
        );
        sessionCookie = updateSessionCookie(sessionCookie, createBoardResult);

        MvcResult renameBoardResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/boards/" + boardId + "/name")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"boardName":"FrontE2E-renamed"}
                                """))
                        .andReturn(),
                "PATCH /api/v1/boards/{boardId}/name"
        );
        sessionCookie = updateSessionCookie(sessionCookie, renameBoardResult);

        MvcResult moveBoardResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/boards/" + boardId + "/move")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"parentBoardId":null,"sortOrder":502}
                                """))
                        .andReturn(),
                "PATCH /api/v1/boards/{boardId}/move"
        );
        sessionCookie = updateSessionCookie(sessionCookie, moveBoardResult);

        MvcResult memoTypesResult = ensureNot5xx(
                mockMvc.perform(get("/api/v1/memo-types").cookie(sessionCookie)).andReturn(),
                "GET /api/v1/memo-types"
        );
        sessionCookie = updateSessionCookie(sessionCookie, memoTypesResult);

        MvcResult createMemoResult = ensureNot5xx(
                mockMvc.perform(post("/api/v1/boards/" + boardId + "/memos")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "memoId":"%s",
                                  "typeId":"TYPE_BASIC",
                                  "content":"front test memo",
                                  "posX":10,
                                  "posY":20,
                                  "width":300,
                                  "height":180,
                                  "zIndex":1
                                }
                                """.formatted(memoId)))
                        .andReturn(),
                "POST /api/v1/boards/{boardId}/memos"
        );
        sessionCookie = updateSessionCookie(sessionCookie, createMemoResult);

        MvcResult getMemosResult = ensureNot5xx(
                mockMvc.perform(get("/api/v1/boards/" + boardId + "/memos")
                        .cookie(sessionCookie))
                        .andReturn(),
                "GET /api/v1/boards/{boardId}/memos"
        );
        sessionCookie = updateSessionCookie(sessionCookie, getMemosResult);

        MvcResult updateContentResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/memos/" + memoId + "/content")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"content":"memo updated"}
                                """))
                        .andReturn(),
                "PATCH /api/v1/memos/{memoId}/content"
        );
        sessionCookie = updateSessionCookie(sessionCookie, updateContentResult);

        MvcResult updatePositionResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/memos/" + memoId + "/position")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"posX":120,"posY":140}
                                """))
                        .andReturn(),
                "PATCH /api/v1/memos/{memoId}/position"
        );
        sessionCookie = updateSessionCookie(sessionCookie, updatePositionResult);

        MvcResult updateSizeResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/memos/" + memoId + "/size")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"width":360,"height":240}
                                """))
                        .andReturn(),
                "PATCH /api/v1/memos/{memoId}/size"
        );
        sessionCookie = updateSessionCookie(sessionCookie, updateSizeResult);

        MvcResult updateZIndexResult = ensureNot5xx(
                mockMvc.perform(patch("/api/v1/boards/" + boardId + "/memos/zindex")
                        .cookie(sessionCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"memos":[{"memoId":"%s","zIndex":2}]}
                                """.formatted(memoId)))
                        .andReturn(),
                "PATCH /api/v1/boards/{boardId}/memos/zindex"
        );
        sessionCookie = updateSessionCookie(sessionCookie, updateZIndexResult);

        MvcResult deleteMemoResult = ensureNot5xx(
                mockMvc.perform(delete("/api/v1/memos/" + memoId).cookie(sessionCookie)).andReturn(),
                "DELETE /api/v1/memos/{memoId}"
        );
        sessionCookie = updateSessionCookie(sessionCookie, deleteMemoResult);

        MvcResult deleteBoardResult = ensureNot5xx(
                mockMvc.perform(delete("/api/v1/boards/" + boardId).cookie(sessionCookie)).andReturn(),
                "DELETE /api/v1/boards/{boardId}"
        );
        sessionCookie = updateSessionCookie(sessionCookie, deleteBoardResult);

        mockMvc.perform(post("/api/v1/auth/logout").cookie(sessionCookie))
                .andExpect(status().isOk());
    }

    private MvcResult ensureNot5xx(MvcResult result, String endpointName) throws Exception {
        int status = result.getResponse().getStatus();
        String body = result.getResponse().getContentAsString();
        assertTrue(status < 500, endpointName + " returned " + status + " body=" + body);
        return result;
    }

    private Cookie updateSessionCookie(Cookie current, MvcResult result) {
        Cookie updated = result.getResponse().getCookie("SESSION");
        return updated != null ? updated : current;
    }
}
