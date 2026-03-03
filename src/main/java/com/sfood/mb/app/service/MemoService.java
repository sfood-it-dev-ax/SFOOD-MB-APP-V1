package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.request.CreateMemoRequest;
import com.sfood.mb.app.dto.request.UpdateMemoContentRequest;
import com.sfood.mb.app.dto.request.UpdateMemoPositionRequest;
import com.sfood.mb.app.dto.request.UpdateMemoSizeRequest;
import com.sfood.mb.app.dto.request.UpdateMemoZIndexRequest;
import com.sfood.mb.app.dto.response.MemoResponse;
import java.util.List;

public interface MemoService {
    List<MemoResponse> list(String userId, String boardId);

    MemoResponse create(String userId, String boardId, CreateMemoRequest request);

    MemoResponse updateContent(String userId, String memoId, UpdateMemoContentRequest request);

    MemoResponse updatePosition(String userId, String memoId, UpdateMemoPositionRequest request);

    MemoResponse updateSize(String userId, String memoId, UpdateMemoSizeRequest request);

    List<MemoResponse> updateZIndex(String userId, String boardId, UpdateMemoZIndexRequest request);

    void delete(String userId, String memoId);
}
