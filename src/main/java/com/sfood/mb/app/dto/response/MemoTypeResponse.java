package com.sfood.mb.app.dto.response;

public record MemoTypeResponse(
    String typeId,
    String typeName,
    String defaultColor,
    String shapeCss,
    Integer sortOrder
) {
}
