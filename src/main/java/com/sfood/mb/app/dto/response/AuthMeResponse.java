package com.sfood.mb.app.dto.response;

public record AuthMeResponse(
    String userId,
    String name,
    String profileImage
) {
}
