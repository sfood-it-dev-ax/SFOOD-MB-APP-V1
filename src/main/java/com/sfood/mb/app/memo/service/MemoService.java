package com.sfood.mb.app.memo.service;

import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.common.BusinessException;
import com.sfood.mb.app.memo.domain.Memo;
import com.sfood.mb.app.memo.domain.MemoRepository;
import com.sfood.mb.app.memo.dto.CreateMemoRequest;
import com.sfood.mb.app.memo.dto.MemoResponse;
import com.sfood.mb.app.memo.dto.UpdateMemoRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final BoardRepository boardRepository;

    public MemoService(MemoRepository memoRepository, BoardRepository boardRepository) {
        this.memoRepository = memoRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<MemoResponse> getMemos(String boardId) {
        validateBoard(boardId);
        return memoRepository.findVisibleByBoardId(boardId)
                .stream()
                .map(MemoResponse::from)
                .toList();
    }

    @Transactional
    public MemoResponse createMemo(CreateMemoRequest request) {
        validateBoard(request.boardId());
        if (memoRepository.existsById(request.memoId())) {
            throw new BusinessException("MEMO_ALREADY_EXISTS", "Memo already exists.");
        }

        Memo memo = new Memo(
                request.memoId(),
                request.boardId(),
                request.memoTypeId(),
                request.content(),
                request.posX() == null ? 0 : request.posX(),
                request.posY() == null ? 0 : request.posY(),
                request.width() == null ? 300 : request.width(),
                request.height() == null ? 200 : request.height(),
                request.zIndex() == null ? 0 : request.zIndex(),
                request.textColor(),
                request.fontFamily(),
                request.fontSize()
        );
        return MemoResponse.from(memoRepository.save(memo));
    }

    @Transactional
    public MemoResponse updateMemo(String memoId, UpdateMemoRequest request) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new BusinessException("MEMO_NOT_FOUND", "Memo not found."));

        if (memo.isHide()) {
            throw new BusinessException("MEMO_NOT_FOUND", "Memo not found.");
        }

        if (request.memoTypeId() != null) {
            memo.updateMemoTypeId(request.memoTypeId());
        }
        if (request.content() != null) {
            memo.updateContent(request.content());
        }
        memo.updatePosition(request.posX(), request.posY());
        memo.updateSize(request.width(), request.height());
        memo.updateZIndex(request.zIndex());
        memo.updateStyle(request.textColor(), request.fontFamily(), request.fontSize());

        return MemoResponse.from(memo);
    }

    @Transactional
    public void deleteMemo(String memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new BusinessException("MEMO_NOT_FOUND", "Memo not found."));

        if (memo.isHide()) {
            throw new BusinessException("MEMO_NOT_FOUND", "Memo not found.");
        }
        memo.markHidden();
    }

    private void validateBoard(String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException("BOARD_NOT_FOUND", "Board not found."));
        if (board.isHide()) {
            throw new BusinessException("BOARD_NOT_FOUND", "Board not found.");
        }
    }
}
