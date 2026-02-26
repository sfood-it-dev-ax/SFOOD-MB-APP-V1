package com.sfood.mb.app.memo.dto;

import com.sfood.mb.app.memo.domain.Memo;

import java.time.LocalDateTime;

public record MemoResponse(
        String memoId,
        String boardId,
        String memoTypeId,
        String content,
        int posX,
        int posY,
        int width,
        int height,
        int zIndex,
        String textColor,
        String fontFamily,
        Integer fontSize,
        boolean hide,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
                memo.getMemoId(),
                memo.getBoardId(),
                memo.getMemoTypeId(),
                memo.getContent(),
                memo.getPosX(),
                memo.getPosY(),
                memo.getWidth(),
                memo.getHeight(),
                memo.getZIndex(),
                memo.getTextColor(),
                memo.getFontFamily(),
                memo.getFontSize(),
                memo.isHide(),
                memo.getCreatedAt(),
                memo.getUpdatedAt()
        );
    }
}
