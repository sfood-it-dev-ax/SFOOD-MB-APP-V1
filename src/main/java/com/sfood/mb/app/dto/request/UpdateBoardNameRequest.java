package com.sfood.mb.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBoardNameRequest(@NotBlank @Size(max = 20) String boardName) {
}
