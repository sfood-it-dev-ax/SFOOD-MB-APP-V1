package com.sfood.mb.app.memo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateMemoRequest(
        @NotBlank(message = "memoId is required")
        @Size(max = 160, message = "memoId max length is 160")
        String memoId,

        @NotBlank(message = "boardId is required")
        @Size(max = 120, message = "boardId max length is 120")
        String boardId,

        @NotBlank(message = "memoTypeId is required")
        @Size(max = 80, message = "memoTypeId max length is 80")
        String memoTypeId,

        @Size(max = 5000, message = "content max length is 5000")
        String content,

        Integer posX,
        Integer posY,
        Integer width,
        Integer height,
        Integer zIndex,

        @Size(max = 40, message = "textColor max length is 40")
        String textColor,

        @Size(max = 120, message = "fontFamily max length is 120")
        String fontFamily,
        Integer fontSize
) {
}
