package com.sfood.mb.app.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardRenameRequest(
        @NotBlank(message = "name is required")
        @Size(max = 20, message = "board name must be <= 20") String name
) {
}
