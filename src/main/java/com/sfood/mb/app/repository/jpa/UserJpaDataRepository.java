package com.sfood.mb.app.repository.jpa;

import com.sfood.mb.app.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaDataRepository extends JpaRepository<User, String> {
}
