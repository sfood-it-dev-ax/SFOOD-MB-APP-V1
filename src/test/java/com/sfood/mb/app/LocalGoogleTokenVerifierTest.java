package com.sfood.mb.app;

import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.service.auth.LocalGoogleTokenVerifier;
import com.sfood.mb.app.service.auth.VerifiedGoogleUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalGoogleTokenVerifierTest {

    private final LocalGoogleTokenVerifier verifier = new LocalGoogleTokenVerifier();

    @Test
    void verify_shouldParseLocalToken() {
        VerifiedGoogleUser user = verifier.verify("tester@example.com|tester|https://img");
        Assertions.assertEquals("tester@example.com", user.userId());
        Assertions.assertEquals("tester", user.name());
        Assertions.assertEquals("https://img", user.profileImage());
    }

    @Test
    void verify_shouldThrowOnInvalidToken() {
        Assertions.assertThrows(ApiException.class, () -> verifier.verify("|bad"));
    }
}
