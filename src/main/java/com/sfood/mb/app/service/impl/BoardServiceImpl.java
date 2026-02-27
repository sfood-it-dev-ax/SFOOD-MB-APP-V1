package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards(String userId) {
        return boardRepository.findActiveByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BoardResponse createBoard(String userId, CreateBoardRequest request) {
        if (boardRepository.findById(request.boardId()).isPresent()) {
            throw new ApiException(ErrorCode.CONFLICT, HttpStatus.CONFLICT, "Board already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        Board board = new Board(
                request.boardId(),
                userId,
                request.parentBoardId(),
                request.boardName(),
                request.sortOrder(),
                now,
                now,
                false
        );
        return toResponse(boardRepository.save(board));
    }

    @Override
    public BoardResponse updateBoardName(String userId, String boardId, UpdateBoardNameRequest request) {
        Board board = getOwnedBoard(userId, boardId);
        board.rename(request.boardName());
        return toResponse(boardRepository.save(board));
    }

    @Override
    public BoardResponse moveBoard(String userId, String boardId, MoveBoardRequest request) {
        Board board = getOwnedBoard(userId, boardId);
        board.move(request.parentBoardId(), request.sortOrder());
        return toResponse(boardRepository.save(board));
    }

    @Override
    public void deleteBoard(String userId, String boardId) {
        Board board = getOwnedBoard(userId, boardId);
        board.hide();
        boardRepository.save(board);
    }

    private Board getOwnedBoard(String userId, String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Board not found"));
        if (board.isHide() || !board.getUserId().equals(userId)) {
            throw new ApiException(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND, "Board not found");
        }
        return board;
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(board.getBoardId(), board.getParentBoardId(), board.getBoardName(), board.getSortOrder());
    }
}
