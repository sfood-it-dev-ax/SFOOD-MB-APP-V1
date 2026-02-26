package com.sfood.mb.app.memo.controller;

import com.sfood.mb.app.memo.dto.CreateMemoRequest;
import com.sfood.mb.app.memo.dto.MemoResponse;
import com.sfood.mb.app.memo.dto.UpdateMemoRequest;
import com.sfood.mb.app.memo.service.MemoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping
    public ResponseEntity<List<MemoResponse>> getMemos(
            @RequestParam
            @NotBlank(message = "boardId is required")
            String boardId
    ) {
        return ResponseEntity.ok(memoService.getMemos(boardId));
    }

    @PostMapping
    public ResponseEntity<MemoResponse> createMemo(@Valid @RequestBody CreateMemoRequest request) {
        return ResponseEntity.ok(memoService.createMemo(request));
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<MemoResponse> updateMemo(
            @PathVariable String memoId,
            @Valid @RequestBody UpdateMemoRequest request
    ) {
        return ResponseEntity.ok(memoService.updateMemo(memoId, request));
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable String memoId) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }
}
