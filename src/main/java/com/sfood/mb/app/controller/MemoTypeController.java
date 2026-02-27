package com.sfood.mb.app.controller;

import com.sfood.mb.app.common.ApiResponse;
import com.sfood.mb.app.common.SessionUserResolver;
import com.sfood.mb.app.dto.response.MemoTypeResponse;
import com.sfood.mb.app.service.MemoTypeService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/memo-types")
public class MemoTypeController {

    private static final Logger log = LoggerFactory.getLogger(MemoTypeController.class);

    private final SessionUserResolver sessionUserResolver;
    private final MemoTypeService memoTypeService;

    public MemoTypeController(SessionUserResolver sessionUserResolver, MemoTypeService memoTypeService) {
        this.sessionUserResolver = sessionUserResolver;
        this.memoTypeService = memoTypeService;
    }

    @GetMapping
    public ApiResponse<List<MemoTypeResponse>> getMemoTypes(HttpSession session) {
        log.info("API IN - GET /api/v1/memo-types (sessionId={})", session.getId());
        sessionUserResolver.resolve(session);
        return ApiResponse.success(memoTypeService.getMemoTypes(), "메모 타입 조회 성공");
    }
}
