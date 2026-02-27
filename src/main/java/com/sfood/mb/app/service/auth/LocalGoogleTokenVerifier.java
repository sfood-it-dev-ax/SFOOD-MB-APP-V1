package com.sfood.mb.app.service.auth;

import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.auth.google-verifier-mode", havingValue = "local", matchIfMissing = true)
public class LocalGoogleTokenVerifier implements GoogleTokenVerifier {

    @Override
    public VerifiedGoogleUser verify(String googleToken) {
        String[] parts = googleToken.split("\\|");
        if (parts.length == 0 || parts[0].isBlank()) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "Invalid googleToken");
        }

        String userId = parts[0].contains("@") ? parts[0] : parts[0] + "@local.test";
        String name = parts.length > 1 && !parts[1].isBlank() ? parts[1] : userId.split("@")[0];
        String profileImage = parts.length > 2 ? parts[2] : "";
        return new VerifiedGoogleUser(userId, name, profileImage);
    }
}
