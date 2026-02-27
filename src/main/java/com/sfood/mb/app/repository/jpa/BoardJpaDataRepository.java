package com.sfood.mb.app.repository.jpa;

import com.sfood.mb.app.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardJpaDataRepository extends JpaRepository<Board, String> {
    List<Board> findByUserIdAndHideFalseOrderBySortOrderAsc(String userId);
}
