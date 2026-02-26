package com.sfood.mb.app.board.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateBoardRequest(
        @NotBlank(message = "boardId is required")
        @Size(max = 120, message = "boardId max length is 120")
        String boardId,

        @NotBlank(message = "userEmail is required")
        @Email(message = "userEmail must be a valid email")
        String userEmail,

        @NotBlank(message = "name is required")
        @Size(max = 20, message = "name max length is 20")
        String name,

        @Size(max = 120, message = "parentBoardId max length is 120")
        String parentBoardId,

        Integer sortOrder
) {
}
