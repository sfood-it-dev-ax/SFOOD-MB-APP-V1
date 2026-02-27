package com.sfood.mb.app.repository.adapter;

import com.sfood.mb.app.domain.Memo;
import com.sfood.mb.app.repository.MemoRepository;
import com.sfood.mb.app.repository.jpa.MemoJpaDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memory")
public class JpaMemoRepositoryAdapter implements MemoRepository {

    private final MemoJpaDataRepository dataRepository;

    public JpaMemoRepositoryAdapter(MemoJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Optional<Memo> findById(String memoId) {
        return dataRepository.findById(memoId);
    }

    @Override
    public List<Memo> findActiveByBoardId(String boardId) {
        return dataRepository.findByBoardIdAndHideFalseOrderByZindexAsc(boardId);
    }

    @Override
    public Memo save(Memo memo) {
        return dataRepository.save(memo);
    }
}
