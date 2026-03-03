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
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.repository.MemoRepository;
import com.sfood.mb.app.repository.MemoTypeRepository;
import com.sfood.mb.app.service.MemoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;
    private final BoardRepository boardRepository;
    private final MemoTypeRepository memoTypeRepository;

    public MemoServiceImpl(MemoRepository memoRepository, BoardRepository boardRepository, MemoTypeRepository memoTypeRepository) {
        this.memoRepository = memoRepository;
        this.boardRepository = boardRepository;
        this.memoTypeRepository = memoTypeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemoResponse> list(String userId, String boardId) {
        Board board = getBoardForUser(userId, boardId);
        return memoRepository.findActiveByBoardIdOrderByZIndexAsc(board.getBoardId()).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public MemoResponse create(String userId, String boardId, CreateMemoRequest request) {
        Board board = getBoardForUser(userId, boardId);
        memoTypeRepository.findById(request.typeId())
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "메모 타입을 찾을 수 없습니다."));
        Memo memo = memoRepository.save(Memo.create(
            request.memoId(), board.getBoardId(), request.typeId(), request.content(), request.posX(), request.posY(),
            request.width(), request.height(), request.zIndex()
        ));
        return toResponse(memo);
    }

    @Override
    @Transactional
    public MemoResponse updateContent(String userId, String memoId, UpdateMemoContentRequest request) {
        Memo memo = getMemoForUser(userId, memoId);
        memo.updateContent(request.content());
        return toResponse(memo);
    }

    @Override
    @Transactional
    public MemoResponse updatePosition(String userId, String memoId, UpdateMemoPositionRequest request) {
        Memo memo = getMemoForUser(userId, memoId);
        memo.updatePosition(request.posX(), request.posY());
        return toResponse(memo);
    }

    @Override
    @Transactional
    public MemoResponse updateSize(String userId, String memoId, UpdateMemoSizeRequest request) {
        Memo memo = getMemoForUser(userId, memoId);
        memo.updateSize(request.width(), request.height());
        return toResponse(memo);
    }

    @Override
    @Transactional
    public List<MemoResponse> updateZIndex(String userId, String boardId, UpdateMemoZIndexRequest request) {
        Board board = getBoardForUser(userId, boardId);
        for (UpdateMemoZIndexRequest.MemoZIndexItem item : request.memos()) {
            Memo memo = memoRepository.findActiveByMemoId(item.memoId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "메모를 찾을 수 없습니다."));
            if (!memo.getBoardId().equals(board.getBoardId())) {
                throw new ApiException(HttpStatus.CONFLICT, "CONFLICT", "보드와 메모가 일치하지 않습니다.");
            }
            memo.updateZIndex(item.zIndex());
        }

        return memoRepository.findActiveByBoardIdOrderByZIndexAsc(board.getBoardId()).stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional
    public void delete(String userId, String memoId) {
        Memo memo = getMemoForUser(userId, memoId);
        memo.hide();
    }

    private Memo getMemoForUser(String userId, String memoId) {
        Memo memo = memoRepository.findActiveByMemoId(memoId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "메모를 찾을 수 없습니다."));
        getBoardForUser(userId, memo.getBoardId());
        return memo;
    }

    private Board getBoardForUser(String userId, String boardId) {
        Board board = boardRepository.findByBoardIdAndHideFalse(boardId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "보드를 찾을 수 없습니다."));
        if (!board.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "접근 권한이 없습니다.");
        }
        return board;
    }

    private MemoResponse toResponse(Memo memo) {
        return new MemoResponse(
            memo.getMemoId(), memo.getBoardId(), memo.getTypeId(), memo.getContent(), memo.getPosX(),
            memo.getPosY(), memo.getWidth(), memo.getHeight(), memo.getZIndex()
        );
    }
}
