package com.sfood.mb.app.repository.jpa;

import com.sfood.mb.app.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoJpaDataRepository extends JpaRepository<Memo, String> {
    List<Memo> findByBoardIdAndHideFalseOrderByZindexAsc(String boardId);
}
