package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.MemoType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoTypeRepository extends JpaRepository<MemoType, String> {
    List<MemoType> findByActiveTrueOrderBySortOrderAsc();
}
