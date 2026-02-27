package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record MoveBoardRequest(String parentBoardId, @NotNull Integer sortOrder) {
}
