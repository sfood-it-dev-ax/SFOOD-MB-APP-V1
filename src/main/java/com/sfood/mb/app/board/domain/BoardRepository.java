package com.sfood.mb.app.board.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String> {
    List<Board> findByUserEmailAndHideFalseOrderByCreatedAtAsc(String userEmail);
}
