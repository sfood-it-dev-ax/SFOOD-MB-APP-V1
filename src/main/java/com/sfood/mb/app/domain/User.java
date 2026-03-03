package com.sfood.mb.app.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, length = 100)
    private String userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 2048)
    private String googleToken;

    protected User() {
    }

    public User(String userId, String name, String googleToken) {
        this.userId = userId;
        this.name = name;
        this.googleToken = googleToken;
    }

    public String userId() {
        return userId;
    }

    public String name() {
        return name;
    }

    public String googleToken() {
        return googleToken;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void bindGoogleTokenIfMissing(String googleToken) {
        if (this.googleToken == null || this.googleToken.isBlank()) {
            this.googleToken = googleToken;
        }
    }
}
