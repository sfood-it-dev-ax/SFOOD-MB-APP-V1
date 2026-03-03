package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.dto.request.CreateMemoRequest;
import com.sfood.mb.app.dto.request.UpdateMemoContentRequest;
import com.sfood.mb.app.dto.request.UpdateMemoPositionRequest;
import com.sfood.mb.app.dto.request.UpdateMemoSizeRequest;
import com.sfood.mb.app.dto.request.UpdateMemoZIndexRequest;
import com.sfood.mb.app.dto.response.MemoResponse;
import com.sfood.mb.app.dto.response.MemoTypeResponse;
import com.sfood.mb.app.service.MemoService;
import com.sfood.mb.app.service.MemoTypeService;
import com.sfood.mb.app.session.SessionUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MemoController {
    private static final Logger log = LoggerFactory.getLogger(MemoController.class);
    private final MemoService memoService;
    private final MemoTypeService memoTypeService;

    public MemoController(MemoService memoService, MemoTypeService memoTypeService) {
        this.memoService = memoService;
        this.memoTypeService = memoTypeService;
    }

    @GetMapping("/boards/{boardId}/memos")
    public ApiResponse<List<MemoResponse>> list(
        @PathVariable("boardId") String boardId,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{}/memos list request - userId={}", boardId, userId);
        List<MemoResponse> response = memoService.list(userId, boardId);
        log.info("API /boards/{}/memos list success - userId={}, memoCount={}", boardId, userId, response.size());
        return ApiResponse.ok(response, "조회 성공");
    }

    @PostMapping("/boards/{boardId}/memos")
    public ApiResponse<MemoResponse> create(
        @PathVariable("boardId") String boardId,
        @Valid @RequestBody CreateMemoRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{}/memos create request - userId={}", boardId, userId);
        MemoResponse response = memoService.create(userId, boardId, request);
        log.info("API /boards/{}/memos create success - userId={}, memoId={}", boardId, userId, response.memoId());
        return ApiResponse.ok(response, "생성 성공");
    }

    @PatchMapping("/memos/{memoId}/content")
    public ApiResponse<MemoResponse> updateContent(
        @PathVariable("memoId") String memoId,
        @RequestBody UpdateMemoContentRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /memos/{}/content request - userId={}", memoId, userId);
        MemoResponse response = memoService.updateContent(userId, memoId, request);
        log.info("API /memos/{}/content success - userId={}", memoId, userId);
        return ApiResponse.ok(response, "수정 성공");
    }

    @PatchMapping("/memos/{memoId}/position")
    public ApiResponse<MemoResponse> updatePosition(
        @PathVariable("memoId") String memoId,
        @Valid @RequestBody UpdateMemoPositionRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /memos/{}/position request - userId={}", memoId, userId);
        MemoResponse response = memoService.updatePosition(userId, memoId, request);
        log.info("API /memos/{}/position success - userId={}", memoId, userId);
        return ApiResponse.ok(response, "수정 성공");
    }

    @PatchMapping("/memos/{memoId}/size")
    public ApiResponse<MemoResponse> updateSize(
        @PathVariable("memoId") String memoId,
        @Valid @RequestBody UpdateMemoSizeRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /memos/{}/size request - userId={}", memoId, userId);
        MemoResponse response = memoService.updateSize(userId, memoId, request);
        log.info("API /memos/{}/size success - userId={}", memoId, userId);
        return ApiResponse.ok(response, "수정 성공");
    }

    @PatchMapping("/boards/{boardId}/memos/zindex")
    public ApiResponse<List<MemoResponse>> updateZIndex(
        @PathVariable("boardId") String boardId,
        @Valid @RequestBody UpdateMemoZIndexRequest request,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /boards/{}/memos/zindex request - userId={}, items={}", boardId, userId, request.memos().size());
        List<MemoResponse> response = memoService.updateZIndex(userId, boardId, request);
        log.info("API /boards/{}/memos/zindex success - userId={}, updatedCount={}", boardId, userId, response.size());
        return ApiResponse.ok(response, "수정 성공");
    }

    @DeleteMapping("/memos/{memoId}")
    public ApiResponse<Void> delete(
        @PathVariable("memoId") String memoId,
        HttpSession session
    ) {
        String userId = SessionUtils.requireUserId(session);
        log.info("API /memos/{} delete request - userId={}", memoId, userId);
        memoService.delete(userId, memoId);
        log.info("API /memos/{} delete success - userId={}", memoId, userId);
        return ApiResponse.ok("삭제 성공");
    }

    @GetMapping("/memo-types")
    public ApiResponse<List<MemoTypeResponse>> memoTypes() {
        log.info("API /memo-types list request");
        List<MemoTypeResponse> response = memoTypeService.listActive();
        log.info("API /memo-types list success - count={}", response.size());
        return ApiResponse.ok(response, "조회 성공");
    }
}
