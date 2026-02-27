package com.sfood.mb.app.repository.jpa;

import com.sfood.mb.app.domain.MemoType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoTypeJpaDataRepository extends JpaRepository<MemoType, String> {
    List<MemoType> findByActiveTrueOrderBySortOrderAsc();
}
