package com.sfood.mb.app.infrastructure.service.impl;

import com.sfood.mb.app.application.service.MemoService;
import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.dto.memo.MemoCreateRequest;
import com.sfood.mb.app.dto.memo.MemoResponse;
import com.sfood.mb.app.dto.memo.MemoTypeResponse;
import com.sfood.mb.app.dto.memo.MemoUpdateRequest;
import com.sfood.mb.app.exception.AppException;
import com.sfood.mb.app.infrastructure.repository.BoardRepository;
import com.sfood.mb.app.infrastructure.repository.MemoRepository;
import com.sfood.mb.app.infrastructure.repository.MemoTypeRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemoServiceImpl implements MemoService {

    private final MemoRepository memoRepository;
    private final BoardRepository boardRepository;
    private final MemoTypeRepository memoTypeRepository;

    public MemoServiceImpl(MemoRepository memoRepository,
                           BoardRepository boardRepository,
                           MemoTypeRepository memoTypeRepository) {
        this.memoRepository = memoRepository;
        this.boardRepository = boardRepository;
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    @Transactional
    public MemoResponse createMemo(String userId, MemoCreateRequest request) {
        Board board = getOwnedActiveBoard(userId, request.boardId());
        if (memoRepository.existsById(request.memoId())) {
            throw new AppException(HttpStatus.CONFLICT, "CONFLICT", "Memo id already exists");
        }
        memoTypeRepository.findById(request.memoTypeId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "MEMO_TYPE_NOT_FOUND", "Memo type not found"));

        Memo memo = new Memo(
                request.memoId(),
                board.getBoardId(),
                request.memoTypeId(),
                request.content(),
                request.x(),
                request.y(),
                request.width(),
                request.height(),
                request.zIndex()
        );
        memoRepository.save(memo);
        return toResponse(memo);
    }

    @Override
    public List<MemoResponse> getMemos(String userId, String boardId) {
        getOwnedActiveBoard(userId, boardId);
        return memoRepository.findByBoardId(boardId).stream()
                .filter(memo -> !memo.isHide())
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public MemoResponse updateMemo(String userId, String memoId, MemoUpdateRequest request) {
        Memo memo = getOwnedActiveMemo(userId, memoId);
        memo.update(request.content(), request.x(), request.y(), request.width(), request.height(), request.zIndex());
        memoRepository.save(memo);
        return toResponse(memo);
    }

    @Override
    @Transactional
    public void deleteMemo(String userId, String memoId) {
        Memo memo = getOwnedActiveMemo(userId, memoId);
        memo.hide();
        memoRepository.save(memo);
    }

    @Override
    public List<MemoTypeResponse> getMemoTypes() {
        return memoTypeRepository.findAll().stream()
                .map(type -> new MemoTypeResponse(type.memoTypeId(), type.label(), type.defaultColor()))
                .toList();
    }

    private Memo getOwnedActiveMemo(String userId, String memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "MEMO_NOT_FOUND", "Memo not found"));
        Board board = getOwnedActiveBoard(userId, memo.getBoardId());
        if (!board.getBoardId().equals(memo.getBoardId()) || memo.isHide()) {
            throw new AppException(HttpStatus.NOT_FOUND, "MEMO_NOT_FOUND", "Memo not found");
        }
        return memo;
    }

    private Board getOwnedActiveBoard(String userId, String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "Board not found"));
        if (!board.getUserId().equals(userId) || board.isHide()) {
            throw new AppException(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "Board not found");
        }
        return board;
    }

    private MemoResponse toResponse(Memo memo) {
        return new MemoResponse(
                memo.getMemoId(),
                memo.getBoardId(),
                memo.getMemoTypeId(),
                memo.getContent(),
                memo.getX(),
                memo.getY(),
                memo.getWidth(),
                memo.getHeight(),
                memo.getZIndex(),
                memo.isHide()
        );
    }
}
