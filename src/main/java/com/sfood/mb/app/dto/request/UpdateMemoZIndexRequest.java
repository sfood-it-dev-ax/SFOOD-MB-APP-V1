package com.sfood.mb.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateMemoZIndexRequest(
    @Valid
    @NotNull(message = "memos는 필수입니다.")
    List<MemoZIndexItem> memos
) {
    public record MemoZIndexItem(
        @NotBlank(message = "memoId는 필수입니다.")
        String memoId,

        @NotNull(message = "zIndex는 필수입니다.")
        Integer zIndex
    ) {
    }
}
