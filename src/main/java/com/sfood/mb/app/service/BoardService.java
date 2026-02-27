package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;

import java.util.List;

public interface BoardService {
    List<BoardResponse> getBoards(String userId);

    BoardResponse createBoard(String userId, CreateBoardRequest request);

    BoardResponse updateBoardName(String userId, String boardId, UpdateBoardNameRequest request);

    BoardResponse moveBoard(String userId, String boardId, MoveBoardRequest request);

    void deleteBoard(String userId, String boardId);
}
