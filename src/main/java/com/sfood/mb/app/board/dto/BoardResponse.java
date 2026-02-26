package com.sfood.mb.app.board.dto;

import com.sfood.mb.app.board.domain.Board;

import java.time.LocalDateTime;

public record BoardResponse(
        String boardId,
        String userEmail,
        String name,
        String parentBoardId,
        int sortOrder,
        boolean hide,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getUserEmail(),
                board.getName(),
                board.getParentBoardId(),
                board.getSortOrder(),
                board.isHide(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }
}
