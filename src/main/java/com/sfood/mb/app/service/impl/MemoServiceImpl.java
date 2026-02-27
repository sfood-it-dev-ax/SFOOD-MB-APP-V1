package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.dto.request.CreateMemoRequest;
import com.sfood.mb.app.dto.request.UpdateMemoContentRequest;
import com.sfood.mb.app.dto.request.UpdateMemoPositionRequest;
import com.sfood.mb.app.dto.request.UpdateMemoSizeRequest;
import com.sfood.mb.app.dto.request.UpdateMemoZIndexRequest;
import com.sfood.mb.app.dto.response.MemoResponse;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.repository.MemoRepository;
import com.sfood.mb.app.repository.MemoTypeRepository;
import com.sfood.mb.app.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;
    private final MemoTypeRepository memoTypeRepository;
    private final BoardRepository boardRepository;

    public MemoServiceImpl(MemoRepository memoRepository, MemoTypeRepository memoTypeRepository, BoardRepository boardRepository) {
        this.memoRepository = memoRepository;
        this.memoTypeRepository = memoTypeRepository;
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(String userId, String boardId) {
        assertBoardOwnership(userId, boardId);
        return memoRepository.findActiveByBoardId(boardId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public MemoResponse createMemo(String userId, String boardId, CreateMemoRequest request) {
        assertBoardOwnership(userId, boardId);
        memoTypeRepository.findById(request.typeId())
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Memo type not found"));
        if (memoRepository.findById(request.memoId()).isPresent()) {
            throw new ApiException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, "Memo already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        Memo memo = new Memo(
                request.memoId(),
                boardId,
                request.typeId(),
                request.content(),
                request.posX(),
                request.posY(),
                request.width(),
                request.height(),
                request.zIndex(),
                now,
                now,
                false
        );
        return toResponse(memoRepository.save(memo));
    }

    @Override
    public MemoResponse updateContent(String userId, String memoId, UpdateMemoContentRequest request) {
        Memo memo = getOwnedMemo(userId, memoId);
        memo.updateContent(request.content());
        return toResponse(memoRepository.save(memo));
    }

    @Override
    public MemoResponse updatePosition(String userId, String memoId, UpdateMemoPositionRequest request) {
        Memo memo = getOwnedMemo(userId, memoId);
        memo.updatePosition(request.posX(), request.posY());
        return toResponse(memoRepository.save(memo));
    }

    @Override
    public MemoResponse updateSize(String userId, String memoId, UpdateMemoSizeRequest request) {
        Memo memo = getOwnedMemo(userId, memoId);
        memo.updateSize(request.width(), request.height());
        return toResponse(memoRepository.save(memo));
    }

    @Override
    public void updateZIndex(String userId, String boardId, UpdateMemoZIndexRequest request) {
        assertBoardOwnership(userId, boardId);
        request.memos().forEach(item -> {
            Memo memo = getOwnedMemo(userId, item.memoId());
            memo.updateZIndex(item.zIndex());
            memoRepository.save(memo);
        });
    }

    @Override
    public void deleteMemo(String userId, String memoId) {
        Memo memo = getOwnedMemo(userId, memoId);
        memo.hide();
        memoRepository.save(memo);
    }

    private Memo getOwnedMemo(String userId, String memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Memo not found"));
        if (memo.isHide()) {
            throw new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Memo not found");
        }
        assertBoardOwnership(userId, memo.getBoardId());
        return memo;
    }

    private void assertBoardOwnership(String userId, String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Board not found"));
        if (board.isHide() || !board.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Board not found");
        }
    }

    private MemoResponse toResponse(Memo memo) {
        return new MemoResponse(
                memo.getMemoId(),
                memo.getBoardId(),
                memo.getTypeId(),
                memo.getContent(),
                memo.getPosX(),
                memo.getPosY(),
                memo.getWidth(),
                memo.getHeight(),
                memo.getZIndex()
        );
    }
}
