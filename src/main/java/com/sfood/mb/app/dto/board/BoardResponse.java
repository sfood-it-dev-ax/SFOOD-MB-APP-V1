package com.sfood.mb.app.dto.board;

public record BoardResponse(
        String boardId,
        String name,
        boolean isHide,
        String parentBoardId,
        int sortOrder
) {
}
