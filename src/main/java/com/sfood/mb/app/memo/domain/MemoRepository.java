package com.sfood.mb.app.memo.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, String> {
    @Query("""
            select m
            from Memo m
            where m.boardId = :boardId
              and m.hide = false
            order by m.zIndex asc, m.createdAt asc
            """)
    List<Memo> findVisibleByBoardId(@Param("boardId") String boardId);
}
