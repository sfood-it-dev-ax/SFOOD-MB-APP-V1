package com.sfood.mb.app.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBoardNameRequest(
        @NotBlank(message = "name is required")
        @Size(max = 20, message = "name max length is 20")
        String name
) {
}
