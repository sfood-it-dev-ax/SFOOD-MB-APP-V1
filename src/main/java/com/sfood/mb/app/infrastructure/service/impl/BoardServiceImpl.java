package com.sfood.mb.app.infrastructure.service.impl;

import com.sfood.mb.app.application.service.BoardService;
import com.sfood.mb.app.domain.Board;
import com.sfood.mb.app.dto.board.BoardCreateRequest;
import com.sfood.mb.app.dto.board.BoardMoveRequest;
import com.sfood.mb.app.dto.board.BoardRenameRequest;
import com.sfood.mb.app.dto.board.BoardResponse;
import com.sfood.mb.app.exception.AppException;
import com.sfood.mb.app.infrastructure.repository.BoardRepository;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    @Transactional
    public BoardResponse createBoard(String userId, BoardCreateRequest request) {
        if (boardRepository.existsById(request.boardId())) {
            throw new AppException(HttpStatus.CONFLICT, "CONFLICT", "Board id already exists");
        }
        Board board = new Board(request.boardId(), userId, request.name());
        boardRepository.save(board);
        return toResponse(board);
    }

    @Override
    public List<BoardResponse> getBoards(String userId) {
        return boardRepository.findByUserId(userId).stream()
                .filter(board -> !board.isHide())
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BoardResponse renameBoard(String userId, String boardId, BoardRenameRequest request) {
        Board board = getOwnedActiveBoard(userId, boardId);
        board.rename(request.name());
        boardRepository.save(board);
        return toResponse(board);
    }

    @Override
    @Transactional
    public BoardResponse moveBoard(String userId, String boardId, BoardMoveRequest request) {
        Board board = getOwnedActiveBoard(userId, boardId);
        String parentBoardId = request.parentBoardId();

        if (parentBoardId != null && parentBoardId.equals(boardId)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Board cannot be its own parent");
        }

        if (parentBoardId != null) {
            getOwnedActiveBoard(userId, parentBoardId);
            validateNoCircularReference(userId, boardId, parentBoardId);
        }

        board.move(parentBoardId, request.sortOrder());
        boardRepository.save(board);
        return toResponse(board);
    }

    @Override
    @Transactional
    public void deleteBoard(String userId, String boardId) {
        Board board = getOwnedActiveBoard(userId, boardId);
        board.hide();
        boardRepository.save(board);
    }

    @Override
    @Transactional
    public void createDefaultBoardIfAbsent(String userId) {
        boolean hasVisibleBoard = boardRepository.findByUserId(userId).stream().anyMatch(board -> !board.isHide());
        if (!hasVisibleBoard) {
            boardRepository.save(new Board(userId + "_default", userId, "My Board"));
        }
    }

    private Board getOwnedActiveBoard(String userId, String boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "Board not found"));
        if (!board.getUserId().equals(userId) || board.isHide()) {
            throw new AppException(HttpStatus.NOT_FOUND, "BOARD_NOT_FOUND", "Board not found");
        }
        return board;
    }

    private void validateNoCircularReference(String userId, String boardId, String parentBoardId) {
        Map<String, Board> boardMap = boardRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(Board::getBoardId, board -> board));

        String currentParent = parentBoardId;
        while (currentParent != null) {
            if (currentParent.equals(boardId)) {
                throw new AppException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Board move would create cycle");
            }
            Board parent = boardMap.get(currentParent);
            if (parent == null || parent.isHide()) {
                break;
            }
            currentParent = parent.getParentBoardId();
        }
    }

    private BoardResponse toResponse(Board board) {
        return new BoardResponse(
                board.getBoardId(),
                board.getName(),
                board.isHide(),
                board.getParentBoardId(),
                board.getSortOrder()
        );
    }
}
