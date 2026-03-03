package com.sfood.mb.app.dto.memo;

public record MemoResponse(
        String memoId,
        String boardId,
        String memoTypeId,
        String content,
        int x,
        int y,
        int width,
        int height,
        int zIndex,
        boolean isHide
) {
}
