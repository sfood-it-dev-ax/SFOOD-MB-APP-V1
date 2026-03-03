package com.sfood.mb.app.infrastructure.repository;

import com.sfood.mb.app.domain.MemoType;
import java.util.List;
import java.util.Optional;

public interface MemoTypeRepository {

    List<MemoType> findAll();

    Optional<MemoType> findById(String memoTypeId);
}
