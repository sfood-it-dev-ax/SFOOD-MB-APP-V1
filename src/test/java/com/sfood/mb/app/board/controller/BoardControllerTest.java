package com.sfood.mb.app.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.board.dto.CreateBoardRequest;
import com.sfood.mb.app.board.dto.UpdateBoardNameRequest;
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
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardRepository.save(new Board("board-1", "user@test.com", "Board 1", null, 0));
        boardRepository.save(new Board("board-2", "user@test.com", "Board 2", "board-1", 1));
    }

    @AfterEach
    void tearDown() {
        boardRepository.deleteAll();
    }

    @Test
    void getBoards_success() throws Exception {
        mockMvc.perform(get("/api/v1/boards").param("userEmail", "user@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].boardId").value("board-1"))
                .andExpect(jsonPath("$[1].boardId").value("board-2"));
    }

    @Test
    void createBoard_success() throws Exception {
        CreateBoardRequest request = new CreateBoardRequest(
                "board-3",
                "user@test.com",
                "Board 3",
                null,
                2
        );

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardId").value("board-3"));

        assertTrue(boardRepository.existsById("board-3"));
    }

    @Test
    void createBoard_validationError() throws Exception {
        CreateBoardRequest request = new CreateBoardRequest("", "invalid", "", null, null);

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void updateBoardName_success() throws Exception {
        mockMvc.perform(patch("/api/v1/boards/board-1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UpdateBoardNameRequest("Renamed"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Renamed"));

        assertEquals("Renamed", boardRepository.findById("board-1").orElseThrow().getName());
    }

    @Test
    void deleteBoard_success() throws Exception {
        mockMvc.perform(delete("/api/v1/boards/board-1"))
                .andExpect(status().isNoContent());

        assertTrue(boardRepository.findById("board-1").orElseThrow().isHide());
    }
}
