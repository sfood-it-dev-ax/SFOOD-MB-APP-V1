package com.sfood.mb.app.application.service;

import com.sfood.mb.app.dto.memo.MemoCreateRequest;
import com.sfood.mb.app.dto.memo.MemoResponse;
import com.sfood.mb.app.dto.memo.MemoTypeResponse;
import com.sfood.mb.app.dto.memo.MemoUpdateRequest;
import java.util.List;

public interface MemoService {

    MemoResponse createMemo(String userId, MemoCreateRequest request);

    List<MemoResponse> getMemos(String userId, String boardId);

    MemoResponse updateMemo(String userId, String memoId, MemoUpdateRequest request);

    void deleteMemo(String userId, String memoId);

    List<MemoTypeResponse> getMemoTypes();
}
