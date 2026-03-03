package com.sfood.mb.app.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalDataSourceConfig {

    @Bean
    @Primary
    public DataSource localDataSource(
        @Value("${app.datasource.url:${spring.datasource.url:}}") String url,
        @Value("${app.datasource.username:${spring.datasource.username:}}") String username,
        @Value("${app.datasource.password:${spring.datasource.password:}}") String configuredPassword,
        @Value("${app.datasource.driver-class-name:${spring.datasource.driver-class-name:org.postgresql.Driver}}") String driverClassName,
        @Value("${app.datasource.schema:${DB_SCHEMA:memo_board_v4}}") String schema,
        Environment environment
    ) {
        String resolvedUrl = requireNonBlank(url, "Datasource URL is required for local profile.");
        String resolvedUsername = requireNonBlank(username, "Datasource username is required for local profile.");
        String password = firstNonBlank(
            configuredPassword,
            environment.getProperty("DB_PASSWORD"),
            environment.getProperty("SPRING_DATASOURCE_PASSWORD"),
            "!@!L1e2e34!!!"
        );

        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(resolvedUrl);
        dataSource.setUsername(resolvedUsername);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setConnectionInitSql("SET search_path TO " + schema);
        return dataSource;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static String requireNonBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(message);
        }
        return value;
    }
}
