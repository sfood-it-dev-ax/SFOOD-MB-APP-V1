package com.sfood.mb.app.board.service;

import com.sfood.mb.app.board.domain.Board;
import com.sfood.mb.app.board.domain.BoardRepository;
import com.sfood.mb.app.board.dto.BoardResponse;
import com.sfood.mb.app.board.dto.CreateBoardRequest;
import com.sfood.mb.app.board.dto.UpdateBoardNameRequest;
import com.sfood.mb.app.common.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards(String userEmail) {
        return boardRepository.findByUserEmailAndHideFalseOrderByCreatedAtAsc(userEmail)
                .stream()
                .map(BoardResponse::from)
                .toList();
    }

    @Transactional
    public BoardResponse createBoard(CreateBoardRequest request) {
        if (boardRepository.existsById(request.boardId())) {
            throw new BusinessException("BOARD_ALREADY_EXISTS", "Board already exists.");
        }

        int sortOrder = request.sortOrder() == null ? 0 : request.sortOrder();
        Board board = new Board(
                request.boardId(),
                request.userEmail(),
                request.name(),
                request.parentBoardId(),
                sortOrder
        );
        return BoardResponse.from(boardRepository.save(board));
    }

    @Transactional
    public BoardResponse updateBoardName(String boardId, UpdateBoardNameRequest request) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException("BOARD_NOT_FOUND", "Board not found."));

        if (board.isHide()) {
            throw new BusinessException("BOARD_NOT_FOUND", "Board not found.");
        }

        board.rename(request.name());
        return BoardResponse.from(board);
    }

    @Transactional
    public void deleteBoard(String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException("BOARD_NOT_FOUND", "Board not found."));

        if (board.isHide()) {
            throw new BusinessException("BOARD_NOT_FOUND", "Board not found.");
        }
        board.markHidden();
    }
}
