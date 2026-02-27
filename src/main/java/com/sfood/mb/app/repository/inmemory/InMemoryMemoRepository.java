package com.sfood.mb.app.repository.inmemory;

import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.repository.MemoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryMemoRepository implements MemoRepository {

    private final Map<String, Memo> memos = new ConcurrentHashMap<>();

    @Override
    public Optional<Memo> findById(String memoId) {
        return Optional.ofNullable(memos.get(memoId));
    }

    @Override
    public List<Memo> findActiveByBoardId(String boardId) {
        return memos.values().stream()
                .filter(memo -> memo.getBoardId().equals(boardId) && !memo.isHide())
                .sorted(Comparator.comparing(Memo::getZIndex))
                .toList();
    }

    @Override
    public Memo save(Memo memo) {
        memos.put(memo.getMemoId(), memo);
        return memo;
    }
}
