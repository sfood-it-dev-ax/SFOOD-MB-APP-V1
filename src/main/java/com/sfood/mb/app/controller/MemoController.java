package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.common.SessionUserResolver;
import com.sfood.mb.app.dto.request.CreateMemoRequest;
import com.sfood.mb.app.dto.request.UpdateMemoContentRequest;
import com.sfood.mb.app.dto.request.UpdateMemoPositionRequest;
import com.sfood.mb.app.dto.request.UpdateMemoSizeRequest;
import com.sfood.mb.app.dto.request.UpdateMemoZIndexRequest;
import com.sfood.mb.app.dto.response.MemoResponse;
import com.sfood.mb.app.service.MemoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MemoController {

    private static final Logger log = LoggerFactory.getLogger(MemoController.class);

    private final SessionUserResolver sessionUserResolver;
    private final MemoService memoService;

    public MemoController(SessionUserResolver sessionUserResolver, MemoService memoService) {
        this.sessionUserResolver = sessionUserResolver;
        this.memoService = memoService;
    }

    @GetMapping("/boards/{boardId}/memos")
    public ApiResponse<List<MemoResponse>> getMemos(@PathVariable("boardId") String boardId, HttpSession session) {
        log.info("API IN - GET /api/v1/boards/{}/memos (sessionId={})", boardId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(memoService.getMemos(userId, boardId), "메모 목록 조회 성공");
    }

    @PostMapping("/boards/{boardId}/memos")
    public ApiResponse<MemoResponse> createMemo(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody CreateMemoRequest request,
            HttpSession session
    ) {
        log.info("API IN - POST /api/v1/boards/{}/memos (memoId={}, content={}, sessionId={})",
                boardId, request.memoId(), summarizeContent(request.content()), session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(memoService.createMemo(userId, boardId, request), "메모 생성 성공");
    }

    @PatchMapping("/memos/{memoId}/content")
    public ApiResponse<MemoResponse> updateContent(
            @PathVariable("memoId") String memoId,
            @Valid @RequestBody UpdateMemoContentRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/memos/{}/content (content={}, sessionId={})",
                memoId, summarizeContent(request.content()), session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(memoService.updateContent(userId, memoId, request), "메모 내용 수정 성공");
    }

    @PatchMapping("/memos/{memoId}/position")
    public ApiResponse<MemoResponse> updatePosition(
            @PathVariable("memoId") String memoId,
            @Valid @RequestBody UpdateMemoPositionRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/memos/{}/position (sessionId={})", memoId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(memoService.updatePosition(userId, memoId, request), "메모 위치 수정 성공");
    }

    @PatchMapping("/memos/{memoId}/size")
    public ApiResponse<MemoResponse> updateSize(
            @PathVariable("memoId") String memoId,
            @Valid @RequestBody UpdateMemoSizeRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/memos/{}/size (sessionId={})", memoId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        return ApiResponse.success(memoService.updateSize(userId, memoId, request), "메모 크기 수정 성공");
    }

    @PatchMapping("/boards/{boardId}/memos/zindex")
    public ApiResponse<Void> updateZIndex(
            @PathVariable("boardId") String boardId,
            @Valid @RequestBody UpdateMemoZIndexRequest request,
            HttpSession session
    ) {
        log.info("API IN - PATCH /api/v1/boards/{}/memos/zindex (count={}, sessionId={})",
                boardId, request.memos() == null ? 0 : request.memos().size(), session.getId());
        String userId = sessionUserResolver.resolve(session);
        memoService.updateZIndex(userId, boardId, request);
        return ApiResponse.success(null, "메모 z-index 수정 성공");
    }

    @DeleteMapping("/memos/{memoId}")
    public ApiResponse<Void> deleteMemo(@PathVariable("memoId") String memoId, HttpSession session) {
        log.info("API IN - DELETE /api/v1/memos/{} (sessionId={})", memoId, session.getId());
        String userId = sessionUserResolver.resolve(session);
        memoService.deleteMemo(userId, memoId);
        return ApiResponse.success(null, "메모 삭제 성공");
    }

    private String summarizeContent(String content) {
        if (content == null) {
            return "null";
        }
        String singleLine = content.replace("\n", "\\n").replace("\r", "\\r");
        int maxLen = 120;
        if (singleLine.length() <= maxLen) {
            return singleLine;
        }
        return singleLine.substring(0, maxLen) + "...";
    }
}
