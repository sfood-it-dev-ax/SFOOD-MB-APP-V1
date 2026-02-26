package com.sfood.mb.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * IDE debug run configs can accidentally pass an empty spring.datasource.password.
 * If that happens, force fallback password from app.datasource.password-fallback.
 */
public class DbPasswordFallbackEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String DEFAULT_FALLBACK_PASSWORD = "!@!L1e2e34!!!";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String datasourcePassword = environment.getProperty("spring.datasource.password");
        String fallbackPassword = environment.getProperty("app.datasource.password-fallback", DEFAULT_FALLBACK_PASSWORD);

        if (fallbackPassword == null || fallbackPassword.isBlank()) {
            return;
        }
        if (datasourcePassword != null && !datasourcePassword.isBlank()) {
            return;
        }

        Map<String, Object> overrides = new HashMap<>();
        overrides.put("spring.datasource.password", fallbackPassword);
        environment.getPropertySources().addFirst(new MapPropertySource("dbPasswordFallback", overrides));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
