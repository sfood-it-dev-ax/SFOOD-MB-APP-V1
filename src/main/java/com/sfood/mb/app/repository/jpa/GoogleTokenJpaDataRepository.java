package com.sfood.mb.app.repository.jpa;

import com.sfood.mb.app.domain.GoogleToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleTokenJpaDataRepository extends JpaRepository<GoogleToken, String> {
}
