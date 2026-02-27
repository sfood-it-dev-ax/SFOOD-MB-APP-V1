package com.sfood.mb.app.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateMemoZIndexRequest(@NotEmpty List<@Valid MemoZIndexItem> memos) {
    public record MemoZIndexItem(@NotBlank String memoId, @NotNull Integer zIndex) {
    }
}
