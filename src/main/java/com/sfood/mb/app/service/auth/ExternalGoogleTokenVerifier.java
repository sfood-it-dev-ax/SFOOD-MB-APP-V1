package com.sfood.mb.app.service.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfood.mb.app.exception.ApiException;
import com.sfood.mb.app.exception.ErrorCode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Component
@ConditionalOnProperty(name = "app.auth.google-verifier-mode", havingValue = "external")
public class ExternalGoogleTokenVerifier implements GoogleTokenVerifier {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper;
    private final String tokenInfoBaseUrl;

    public ExternalGoogleTokenVerifier(
            ObjectMapper objectMapper,
            @Value("${app.auth.google-token-info-url:https://oauth2.googleapis.com/tokeninfo}") String tokenInfoBaseUrl
    ) {
        this.objectMapper = objectMapper;
        this.tokenInfoBaseUrl = tokenInfoBaseUrl;
    }

    @Override
    public VerifiedGoogleUser verify(String googleToken) {
        try {
            String encodedToken = UriUtils.encodeQueryParam(googleToken, StandardCharsets.UTF_8);
            URI uri = URI.create(tokenInfoBaseUrl + "?id_token=" + encodedToken);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "Invalid googleToken");
            }

            JsonNode node = objectMapper.readTree(response.body());
            String email = node.path("email").asText("");
            if (email.isBlank()) {
                throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "Invalid googleToken");
            }

            String name = node.path("name").asText(email.split("@")[0]);
            String picture = node.path("picture").asText("");
            return new VerifiedGoogleUser(email, name, picture);
        } catch (ApiException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApiException(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "Failed to verify googleToken");
        }
    }
}
