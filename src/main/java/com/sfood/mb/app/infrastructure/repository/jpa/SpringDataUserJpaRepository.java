package com.sfood.mb.app.infrastructure.repository.jpa;

import com.sfood.mb.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserJpaRepository extends JpaRepository<User, String> {
}
