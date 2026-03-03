package com.sfood.mb.app.infrastructure.repository.jpa;

import com.sfood.mb.app.domain.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataBoardJpaRepository extends JpaRepository<Board, String> {

    List<Board> findByUserIdOrderByCreatedAtAsc(String userId);
}
