package com.sfood.mb.app.repository.adapter;

import com.sfood.mb.app.domain.User;
import com.sfood.mb.app.repository.UserRepository;
import com.sfood.mb.app.repository.jpa.UserJpaDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("!memory")
public class JpaUserRepositoryAdapter implements UserRepository {

    private final UserJpaDataRepository dataRepository;

    public JpaUserRepositoryAdapter(UserJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Optional<User> findById(String userId) {
        return dataRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return dataRepository.save(user);
    }
}
