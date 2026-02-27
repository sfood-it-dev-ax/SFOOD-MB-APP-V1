package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.MemoType;

import java.util.List;
import java.util.Optional;

public interface MemoTypeRepository {
    List<MemoType> findActive();

    Optional<MemoType> findById(String typeId);
}
