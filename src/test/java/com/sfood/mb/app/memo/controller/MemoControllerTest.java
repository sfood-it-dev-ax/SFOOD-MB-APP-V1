package com.sfood.mb.app.memo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.memo.domain.Memo;
import com.sfood.mb.app.memo.domain.MemoRepository;
import com.sfood.mb.app.memo.dto.CreateMemoRequest;
import com.sfood.mb.app.memo.dto.UpdateMemoRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemoRepository memoRepository;

    @BeforeEach
    void setUp() {
        boardRepository.save(new Board("board-memo", "user@test.com", "Memo Board", null, 0));
        memoRepository.save(new Memo(
                "memo-a",
                "board-memo",
                "type-basic",
                "memo A",
                10,
                20,
                300,
                200,
                1,
                "#111",
                "Arial",
                14
        ));
        memoRepository.save(new Memo(
                "memo-b",
                "board-memo",
                "type-basic",
                "memo B",
                40,
                60,
                320,
                210,
                2,
                "#222",
                "Arial",
                14
        ));
    }

    @AfterEach
    void tearDown() {
        memoRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    void getMemos_success() throws Exception {
        mockMvc.perform(get("/api/v1/memos").param("boardId", "board-memo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].memoId").value("memo-a"))
                .andExpect(jsonPath("$[1].memoId").value("memo-b"));
    }

    @Test
    void createMemo_success() throws Exception {
        CreateMemoRequest request = new CreateMemoRequest(
                "memo-c",
                "board-memo",
                "type-task",
                "new memo",
                100,
                120,
                340,
                220,
                3,
                "#333333",
                "Pretendard",
                15
        );

        mockMvc.perform(post("/api/v1/memos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memoId").value("memo-c"));

        assertTrue(memoRepository.existsById("memo-c"));
    }

    @Test
    void updateMemo_success() throws Exception {
        UpdateMemoRequest request = new UpdateMemoRequest(
                "type-task",
                "updated memo",
                150,
                170,
                350,
                230,
                5,
                "#999999",
                "Pretendard",
                18
        );

        mockMvc.perform(patch("/api/v1/memos/memo-a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("updated memo"))
                .andExpect(jsonPath("$.zIndex").value(5));

        Memo memo = memoRepository.findById("memo-a").orElseThrow();
        assertEquals(150, memo.getPosX());
    }

    @Test
    void deleteMemo_success() throws Exception {
        mockMvc.perform(delete("/api/v1/memos/memo-a"))
                .andExpect(status().isNoContent());

        Memo memo = memoRepository.findById("memo-a").orElseThrow();
        assertTrue(memo.isHide());
    }
}
