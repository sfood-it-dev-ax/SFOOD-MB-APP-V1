package com.sfood.mb.app.service;

import com.sfood.mb.app.dto.request.CreateBoardRequest;
import com.sfood.mb.app.dto.request.MoveBoardRequest;
import com.sfood.mb.app.dto.request.UpdateBoardNameRequest;
import com.sfood.mb.app.dto.response.BoardResponse;
import java.util.List;

public interface BoardService {
    List<BoardResponse> list(String userId);

    BoardResponse create(String userId, CreateBoardRequest request);

    BoardResponse updateName(String userId, String boardId, UpdateBoardNameRequest request);

    BoardResponse move(String userId, String boardId, MoveBoardRequest request);

    void delete(String userId, String boardId);
}
