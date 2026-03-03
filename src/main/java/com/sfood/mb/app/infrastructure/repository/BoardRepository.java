package com.sfood.mb.app.infrastructure.repository;

import com.sfood.mb.app.domain.Board;
import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findById(String boardId);

    List<Board> findByUserId(String userId);

    Board save(Board board);

    boolean existsById(String boardId);
}
