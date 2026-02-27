package com.sfood.mb.app.dto.response;

public record MemoResponse(
        String memoId,
        String boardId,
        String typeId,
        String content,
        float posX,
        float posY,
        float width,
        float height,
        int zIndex
) {
}
