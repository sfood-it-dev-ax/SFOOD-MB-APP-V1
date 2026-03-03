package com.sfood.mb.app.infrastructure.repository.jpa;

import com.sfood.mb.app.domain.Memo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemoJpaRepository extends JpaRepository<Memo, String> {

    List<Memo> findByBoardIdOrderByZindexAsc(String boardId);
}
