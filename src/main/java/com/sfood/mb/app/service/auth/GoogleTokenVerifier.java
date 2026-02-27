package com.sfood.mb.app.service.auth;

public interface GoogleTokenVerifier {
    VerifiedGoogleUser verify(String googleToken);
}
