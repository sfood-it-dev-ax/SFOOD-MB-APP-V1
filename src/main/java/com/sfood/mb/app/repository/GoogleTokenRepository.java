package com.sfood.mb.app.repository;

import com.sfood.mb.app.domain.GoogleToken;

import java.util.Optional;

public interface GoogleTokenRepository {
    Optional<GoogleToken> findByUserId(String userId);

    GoogleToken save(GoogleToken googleToken);
}
