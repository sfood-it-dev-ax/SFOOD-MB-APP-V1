package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String userId);

    User save(User user);
}
