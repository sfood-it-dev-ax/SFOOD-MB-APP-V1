package com.sfood.mb.app.repository.adapter;

import com.sfood.mb.app.domain.MemoType;
import com.sfood.mb.app.repository.MemoTypeRepository;
import com.sfood.mb.app.repository.jpa.MemoTypeJpaDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memory")
public class JpaMemoTypeRepositoryAdapter implements MemoTypeRepository {

    private final MemoTypeJpaDataRepository dataRepository;

    public JpaMemoTypeRepositoryAdapter(MemoTypeJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public List<MemoType> findActive() {
        return dataRepository.findByActiveTrueOrderBySortOrderAsc();
    }

    @Override
    public Optional<MemoType> findById(String typeId) {
        return dataRepository.findById(typeId);
    }
}
