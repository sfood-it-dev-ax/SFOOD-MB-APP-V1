package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoRepository {
    Optional<Memo> findById(String memoId);

    List<Memo> findActiveByBoardId(String boardId);

    Memo save(Memo memo);
}
