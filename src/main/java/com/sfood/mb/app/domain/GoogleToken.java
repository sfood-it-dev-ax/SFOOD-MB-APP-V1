package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "mb_google_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoogleToken {

    @Id
    @Column(name = "user_id", nullable = false, length = 255)
    private String userId;

    @Column(name = "google_token", nullable = false, columnDefinition = "TEXT")
    private String googleToken;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public GoogleToken(String userId, String googleToken, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.googleToken = googleToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateToken(String googleToken) {
        this.googleToken = googleToken;
        this.updatedAt = LocalDateTime.now();
    }
}
