package com.sfood.mb.app.controller;

import com.sfood.mb.app.application.service.BoardService;
import com.sfood.mb.app.dto.board.BoardCreateRequest;
import com.sfood.mb.app.dto.board.BoardMoveRequest;
import com.sfood.mb.app.dto.board.BoardRenameRequest;
import com.sfood.mb.app.dto.board.BoardResponse;
import com.sfood.mb.app.dto.common.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ApiResponse<BoardResponse> createBoard(@Valid @RequestBody BoardCreateRequest request, HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(boardService.createBoard(userId, request));
    }

    @GetMapping
    public ApiResponse<List<BoardResponse>> getBoards(HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(boardService.getBoards(userId));
    }

    @PatchMapping("/{boardId}/name")
    public ApiResponse<BoardResponse> renameBoard(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody BoardRenameRequest request,
            HttpSession session
    ) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(boardService.renameBoard(userId, boardId, request));
    }

    @PatchMapping("/{boardId}/move")
    public ApiResponse<BoardResponse> moveBoard(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody BoardMoveRequest request,
            HttpSession session
    ) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(boardService.moveBoard(userId, boardId, request));
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable("boardId") String boardId, HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        boardService.deleteBoard(userId, boardId);
        return ApiResponse.ok(null);
    }
}
