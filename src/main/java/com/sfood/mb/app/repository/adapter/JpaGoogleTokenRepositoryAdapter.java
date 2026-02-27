package com.sfood.mb.app.repository.adapter;

import com.sfood.mb.app.domain.GoogleToken;
import com.sfood.mb.app.repository.GoogleTokenRepository;
import com.sfood.mb.app.repository.jpa.GoogleTokenJpaDataRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Profile("!memory")
public class JpaGoogleTokenRepositoryAdapter implements GoogleTokenRepository {

    private final GoogleTokenJpaDataRepository dataRepository;

    public JpaGoogleTokenRepositoryAdapter(GoogleTokenJpaDataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public Optional<GoogleToken> findByUserId(String userId) {
        return dataRepository.findById(userId);
    }

    @Override
    public GoogleToken save(GoogleToken googleToken) {
        return dataRepository.save(googleToken);
    }
}
