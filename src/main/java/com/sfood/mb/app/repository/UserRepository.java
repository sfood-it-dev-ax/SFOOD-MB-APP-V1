package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserIdAndHideFalse(String userId);
}
