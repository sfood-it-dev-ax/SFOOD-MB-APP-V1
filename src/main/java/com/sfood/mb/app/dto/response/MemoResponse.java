package com.sfood.mb.app.dto.response;

public record MemoResponse(
    String memoId,
    String boardId,
    String typeId,
    String content,
    Double posX,
    Double posY,
    Double width,
    Double height,
    Integer zIndex
) {
}
