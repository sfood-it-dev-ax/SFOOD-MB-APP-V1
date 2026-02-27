package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Optional<Board> findById(String boardId);

    List<Board> findActiveByUserId(String userId);

    Board save(Board board);
}
