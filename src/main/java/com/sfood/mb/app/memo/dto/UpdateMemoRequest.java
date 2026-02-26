package com.sfood.mb.app.memo.dto;

import jakarta.validation.constraints.Size;

public record UpdateMemoRequest(
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
