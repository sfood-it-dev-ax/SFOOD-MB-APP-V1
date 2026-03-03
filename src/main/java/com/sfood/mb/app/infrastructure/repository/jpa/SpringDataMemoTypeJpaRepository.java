package com.sfood.mb.app.infrastructure.repository.jpa;

import com.sfood.mb.app.domain.MemoType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemoTypeJpaRepository extends JpaRepository<MemoType, String> {
}
