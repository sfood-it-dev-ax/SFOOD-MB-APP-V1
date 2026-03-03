package com.sfood.mb.app.application.service;

import com.sfood.mb.app.dto.board.BoardCreateRequest;
import com.sfood.mb.app.dto.board.BoardMoveRequest;
import com.sfood.mb.app.dto.board.BoardRenameRequest;
import com.sfood.mb.app.dto.board.BoardResponse;
import java.util.List;

public interface BoardService {

    BoardResponse createBoard(String userId, BoardCreateRequest request);

    List<BoardResponse> getBoards(String userId);

    BoardResponse renameBoard(String userId, String boardId, BoardRenameRequest request);

    BoardResponse moveBoard(String userId, String boardId, BoardMoveRequest request);

    void deleteBoard(String userId, String boardId);

    void createDefaultBoardIfAbsent(String userId);
}
