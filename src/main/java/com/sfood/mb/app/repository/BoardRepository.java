package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String> {
    List<Board> findByUserIdAndHideFalseOrderBySortOrderAsc(String userId);

    Optional<Board> findByBoardIdAndHideFalse(String boardId);
}
