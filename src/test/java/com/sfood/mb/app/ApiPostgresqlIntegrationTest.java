package com.sfood.mb.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
class ApiPostgresqlIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void fullApiFlowWithSupabasePostgresql() throws Exception {
        String suffix = String.valueOf(Instant.now().toEpochMilli());
        String account = "tester+" + suffix + "@sfood.com";
        String boardId = account + "_ab12!cd34";
        String memoId = boardId + "_xy12#zz90";

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                .contentType("application/json")
                .content("""
                    {
                      \"googleAccount\": \"%s\",
                      \"token\": \"dummy-token\",
                      \"name\": \"Tester\"
                    }
                    """.formatted(account)))
            .andExpect(status().isOk())
            .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);
        assertThat(session).isNotNull();

        Integer userCount = jdbcTemplate.queryForObject(
            "select count(*) from users where user_id = ?", Integer.class, account
        );
        assertThat(userCount).isEqualTo(1);

        mockMvc.perform(get("/api/v1/auth/me").session(session))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/boards")
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"boardId\": \"%s\",
                      \"parentBoardId\": null,
                      \"boardName\": \"업무보드\",
                      \"sortOrder\": 1
                    }
                    """.formatted(boardId)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/boards").session(session))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/boards/{boardId}/name", boardId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"boardName\": \"업무보드-수정\"
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/boards/{boardId}/move", boardId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"parentBoardId\": null,
                      \"sortOrder\": 2
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/memo-types").session(session))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/boards/{boardId}/memos", boardId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"memoId\": \"%s\",
                      \"typeId\": \"TYPE_BASIC\",
                      \"content\": \"초기 메모\",
                      \"posX\": 100,
                      \"posY\": 120,
                      \"width\": 240,
                      \"height\": 180,
                      \"zIndex\": 1
                    }
                    """.formatted(memoId)))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/boards/{boardId}/memos", boardId).session(session))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/memos/{memoId}/content", memoId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"content\": \"수정 메모\"
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/memos/{memoId}/position", memoId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"posX\": 200,
                      \"posY\": 240
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/memos/{memoId}/size", memoId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"width\": 320,
                      \"height\": 260
                    }
                    """))
            .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/boards/{boardId}/memos/zindex", boardId)
                .session(session)
                .contentType("application/json")
                .content("""
                    {
                      \"memos\": [
                        { \"memoId\": \"%s\", \"zIndex\": 3 }
                      ]
                    }
                    """.formatted(memoId)))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/memos/{memoId}", memoId).session(session))
            .andExpect(status().isOk());

        mockMvc.perform(delete("/api/v1/boards/{boardId}", boardId).session(session))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/logout").session(session))
            .andExpect(status().isOk());

        Integer hiddenBoardCount = jdbcTemplate.queryForObject(
            "select count(*) from boards where board_id = ? and is_hide = true", Integer.class, boardId);
        Integer hiddenMemoCount = jdbcTemplate.queryForObject(
            "select count(*) from memos where memo_id = ? and is_hide = true", Integer.class, memoId);

        assertThat(hiddenBoardCount).isEqualTo(1);
        assertThat(hiddenMemoCount).isEqualTo(1);

        MvcResult unauthorizedResult = mockMvc.perform(get("/api/v1/auth/me"))
            .andExpect(status().isUnauthorized())
            .andReturn();

        JsonNode unauthorizedBody = objectMapper.readTree(unauthorizedResult.getResponse().getContentAsString());
        assertThat(unauthorizedBody.get("errorCode").asText()).isEqualTo("UNAUTHORIZED");
    }
}
