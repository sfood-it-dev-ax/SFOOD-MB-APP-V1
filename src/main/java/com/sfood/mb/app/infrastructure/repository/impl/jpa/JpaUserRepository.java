package com.sfood.mb.app.infrastructure.repository.impl.jpa;

import com.sfood.mb.app.domain.User;
import com.sfood.mb.app.infrastructure.repository.UserRepository;
import com.sfood.mb.app.infrastructure.repository.jpa.SpringDataUserJpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepository implements UserRepository {

    private final SpringDataUserJpaRepository jpaRepository;

    public JpaUserRepository(SpringDataUserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findById(String userId) {
        return jpaRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return jpaRepository.save(user);
    }
}
