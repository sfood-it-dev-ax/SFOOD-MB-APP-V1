package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.Memo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoRepository extends JpaRepository<Memo, String> {
    @Query("select m from Memo m where m.boardId = :boardId and m.hide = false order by m.zIndex asc")
    List<Memo> findActiveByBoardIdOrderByZIndexAsc(@Param("boardId") String boardId);

    @Query("select m from Memo m where m.memoId = :memoId and m.hide = false")
    Optional<Memo> findActiveByMemoId(@Param("memoId") String memoId);
}
