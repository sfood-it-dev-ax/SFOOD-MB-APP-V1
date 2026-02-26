package com.sfood.mb.app.auth.session;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionStore {

    private static final long SESSION_HOURS = 24L;
    private final ConcurrentHashMap<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    public SessionInfo create(String userEmail, String userName) {
        String token = UUID.randomUUID().toString();
        SessionInfo sessionInfo = new SessionInfo(
                token,
                userEmail,
                userName,
                LocalDateTime.now().plusHours(SESSION_HOURS)
        );
        sessions.put(token, sessionInfo);
        return sessionInfo;
    }

    public Optional<SessionInfo> findValid(String token) {
        cleanupExpired();
        SessionInfo sessionInfo = sessions.get(token);
        if (sessionInfo == null) {
            return Optional.empty();
        }
        if (sessionInfo.expiresAt().isBefore(LocalDateTime.now())) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(sessionInfo);
    }

    public void invalidate(String token) {
        sessions.remove(token);
    }

    public void clearAll() {
        sessions.clear();
    }

    private void cleanupExpired() {
        LocalDateTime now = LocalDateTime.now();
        sessions.entrySet().removeIf(entry -> entry.getValue().expiresAt().isBefore(now));
    }
}
