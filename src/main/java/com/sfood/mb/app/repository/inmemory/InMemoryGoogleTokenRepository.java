package com.sfood.mb.app.repository.inmemory;

import com.sfood.mb.app.domain.GoogleToken;
import com.sfood.mb.app.repository.GoogleTokenRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Profile("memory")
public class InMemoryGoogleTokenRepository implements GoogleTokenRepository {

    private final Map<String, GoogleToken> tokens = new ConcurrentHashMap<>();

    @Override
    public Optional<GoogleToken> findByUserId(String userId) {
        return Optional.ofNullable(tokens.get(userId));
    }

    @Override
    public GoogleToken save(GoogleToken googleToken) {
        tokens.put(googleToken.getUserId(), googleToken);
        return googleToken;
    }
}
