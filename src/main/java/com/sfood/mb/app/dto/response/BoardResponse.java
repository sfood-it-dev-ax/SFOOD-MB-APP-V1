package com.sfood.mb.app.dto.response;

public record BoardResponse(String boardId, String parentBoardId, String boardName, int sortOrder) {
}
