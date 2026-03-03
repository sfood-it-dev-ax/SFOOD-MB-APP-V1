package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;
import com.sfood.mb.app.service.BoardService;
import com.sfood.mb.app.session.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
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

@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {
    private static final Logger log = LoggerFactory.getLogger(BoardController.class);
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ApiResponse<List<BoardResponse>> list(HttpSession session) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards list request - userId={}", userId);
        List<BoardResponse> response = boardService.list(userId);
        log.info("API /boards list success - userId={}, boardCount={}", userId, response.size());
        return ApiResponse.ok(response, "조회 성공");
    }

    @PostMapping
    public ApiResponse<BoardResponse> create(@Valid @RequestBody CreateBoardRequest request, HttpSession session) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards create request - userId={}, boardId={}", userId, request.boardId());
        BoardResponse response = boardService.create(userId, request);
        log.info("API /boards create success - userId={}, boardId={}", userId, response.boardId());
        return ApiResponse.ok(response, "생성 성공");
    }

    @PatchMapping("/{boardId}/name")
    public ApiResponse<BoardResponse> updateName(
        @PathVariable("boardId") String boardId,
        @Valid @RequestBody UpdateBoardNameRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{}/name request - userId={}", boardId, userId);
        BoardResponse response = boardService.updateName(userId, boardId, request);
        log.info("API /boards/{}/name success - userId={}", boardId, userId);
        return ApiResponse.ok(response, "수정 성공");
    }

    @PatchMapping("/{boardId}/move")
    public ApiResponse<BoardResponse> move(
        @PathVariable("boardId") String boardId,
        @Valid @RequestBody MoveBoardRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{}/move request - userId={}", boardId, userId);
        BoardResponse response = boardService.move(userId, boardId, request);
        log.info("API /boards/{}/move success - userId={}", boardId, userId);
        return ApiResponse.ok(response, "이동 성공");
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> delete(@PathVariable("boardId") String boardId, HttpSession session) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{} delete request - userId={}", boardId, userId);
        boardService.delete(userId, boardId);
        log.info("API /boards/{} delete success - userId={}", boardId, userId);
        return ApiResponse.ok("삭제 성공");
    }
}
