package com.sfood.mb.app.infrastructure.repository.impl.jpa;

import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.infrastructure.repository.MemoRepository;
import com.sfood.mb.app.infrastructure.repository.jpa.SpringDataMemoJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMemoRepository implements MemoRepository {

    private final SpringDataMemoJpaRepository jpaRepository;

    public JpaMemoRepository(SpringDataMemoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Memo> findById(String memoId) {
        return jpaRepository.findById(memoId);
    }

    @Override
    public List<Memo> findByBoardId(String boardId) {
        return jpaRepository.findByBoardIdOrderByZindexAsc(boardId);
    }

    @Override
    public Memo save(Memo memo) {
        return jpaRepository.save(memo);
    }

    @Override
    public boolean existsById(String memoId) {
        return jpaRepository.existsById(memoId);
    }
}
