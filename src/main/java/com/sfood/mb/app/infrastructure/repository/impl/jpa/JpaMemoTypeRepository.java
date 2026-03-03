package com.sfood.mb.app.infrastructure.repository.impl.jpa;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.infrastructure.repository.MemoTypeRepository;
import com.sfood.mb.app.infrastructure.repository.jpa.SpringDataMemoTypeJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMemoTypeRepository implements MemoTypeRepository {

    private final SpringDataMemoTypeJpaRepository jpaRepository;

    public JpaMemoTypeRepository(SpringDataMemoTypeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<MemoType> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<MemoType> findById(String memoTypeId) {
        return jpaRepository.findById(memoTypeId);
    }
}
