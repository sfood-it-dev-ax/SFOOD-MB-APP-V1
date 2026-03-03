package com.sfood.mb.app.controller;

import com.sfood.mb.app.application.service.MemoService;
import com.sfood.mb.app.dto.common.ApiResponse;
import com.sfood.mb.app.dto.memo.MemoCreateRequest;
import com.sfood.mb.app.dto.memo.MemoResponse;
import com.sfood.mb.app.dto.memo.MemoTypeResponse;
import com.sfood.mb.app.dto.memo.MemoUpdateRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping("/memo-types")
    public ApiResponse<List<MemoTypeResponse>> getMemoTypes(HttpSession session) {
        SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(memoService.getMemoTypes());
    }

    @PostMapping("/memos")
    public ApiResponse<MemoResponse> createMemo(@Valid @RequestBody MemoCreateRequest request, HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(memoService.createMemo(userId, request));
    }

    @GetMapping("/memos")
    public ApiResponse<List<MemoResponse>> getMemos(@RequestParam("boardId") String boardId, HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(memoService.getMemos(userId, boardId));
    }

    @PatchMapping("/memos/{memoId}")
    public ApiResponse<MemoResponse> updateMemo(
            @PathVariable("memoId") String memoId,
            @Valid @RequestBody MemoUpdateRequest request,
            HttpSession session
    ) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        return ApiResponse.ok(memoService.updateMemo(userId, memoId, request));
    }

    @DeleteMapping("/memos/{memoId}")
    public ApiResponse<Void> deleteMemo(@PathVariable("memoId") String memoId, HttpSession session) {
        String userId = SessionAuthSupport.getRequiredUserId(session);
        memoService.deleteMemo(userId, memoId);
        return ApiResponse.ok(null);
    }
}
