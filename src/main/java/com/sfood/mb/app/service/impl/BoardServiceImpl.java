package com.sfood.mb.app.service.impl;

import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.repository.BoardRepository;
import com.sfood.mb.app.service.BoardService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponse> list(String userId) {
        return boardRepository.findByUserIdAndHideFalseOrderBySortOrderAsc(userId).stream()
            .map(b -> new BoardResponse(b.getBoardId(), b.getParentBoardId(), b.getBoardName(), b.getSortOrder()))
            .toList();
    }

    @Override
    @Transactional
    public BoardResponse create(String userId, CreateBoardRequest request) {
        Board board = boardRepository.save(
            Board.create(request.boardId(), userId, request.parentBoardId(), request.boardName(), request.sortOrder())
        );
        return new BoardResponse(board.getBoardId(), board.getParentBoardId(), board.getBoardName(), board.getSortOrder());
    }

    @Override
    @Transactional
    public BoardResponse updateName(String userId, String boardId, UpdateBoardNameRequest request) {
        Board board = getBoardForUser(userId, boardId);
        board.rename(request.boardName());
        return new BoardResponse(board.getBoardId(), board.getParentBoardId(), board.getBoardName(), board.getSortOrder());
    }

    @Override
    @Transactional
    public BoardResponse move(String userId, String boardId, MoveBoardRequest request) {
        Board board = getBoardForUser(userId, boardId);
        board.move(request.parentBoardId(), request.sortOrder());
        return new BoardResponse(board.getBoardId(), board.getParentBoardId(), board.getBoardName(), board.getSortOrder());
    }

    @Override
    @Transactional
    public void delete(String userId, String boardId) {
        Board board = getBoardForUser(userId, boardId);
        board.hide();
    }

    private Board getBoardForUser(String userId, String boardId) {
        Board board = boardRepository.findByBoardIdAndHideFalse(boardId)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "보드를 찾을 수 없습니다."));
        if (!board.getUserId().equals(userId)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "접근 권한이 없습니다.");
        }
        return board;
    }
}
