package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.common.SessionUserResolver;
import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;
import com.sfood.mb.app.service.BoardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private static final Logger log = LoggerFactory.getLogger(BoardController.class);

    private final SessionUserResolver sessionUserResolver;
    private final BoardService boardService;

    public BoardController(SessionUserResolver sessionUserResolver, BoardService boardService) {
        this.sessionUserResolver = sessionUserResolver;
        this.boardService = boardService;
    }

    @GetMapping
    public ApiResponse<List<BoardResponse>> getBoards(HttpSession session) {
        log.info("API IN - GET /api/v1/boards (sessionId={})", session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(boardService.getBoards(userId), "보드 목록 조회 성공");
    }

    @PostMapping
    public ApiResponse<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request, HttpSession session) {
        log.info("API IN - POST /api/v1/boards (boardId={}, sessionId={})", request.boardId(), session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(boardService.createBoard(userId, request), "보드 생성 성공");
    }

    @PatchMapping("/{boardId}/name")
    public ApiResponse<BoardResponse> updateBoardName(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody UpdateBoardNameRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/boards/{}/name (sessionId={})", boardId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(boardService.updateBoardName(userId, boardId, request), "보드 이름 수정 성공");
    }

    @PatchMapping("/{boardId}/move")
    public ApiResponse<BoardResponse> moveBoard(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody MoveBoardRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/boards/{}/move (sessionId={})", boardId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(boardService.moveBoard(userId, boardId, request), "보드 이동 성공");
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardId") String boardId, HttpSession session) {
        log.info("API IN - DELETE /api/v1/boards/{} (sessionId={})", boardId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        boardService.deleteBoard(userId, boardId);
        return ApiResponse.success(null, "보드 삭제 성공");
    }
}
