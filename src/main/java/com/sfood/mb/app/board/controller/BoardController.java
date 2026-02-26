package com.sfood.mb.app.board.controller;

import com.sfood.mb.app.board.dto.BoardResponse;
import com.sfood.mb.app.board.dto.CreateBoardRequest;
import com.sfood.mb.app.board.dto.UpdateBoardNameRequest;
import com.sfood.mb.app.board.service.BoardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards(
            @RequestParam
            @NotBlank(message = "userEmail is required")
            @Email(message = "userEmail must be a valid email")
            String userEmail
    ) {
        return ResponseEntity.ok(boardService.getBoards(userEmail));
    }

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) {
        return ResponseEntity.ok(boardService.createBoard(request));
    }

    @PatchMapping("/{boardId}/name")
    public ResponseEntity<BoardResponse> updateBoardName(
            @PathVariable String boardId,
            @Valid @RequestBody UpdateBoardNameRequest request
    ) {
        return ResponseEntity.ok(boardService.updateBoardName(boardId, request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
}
