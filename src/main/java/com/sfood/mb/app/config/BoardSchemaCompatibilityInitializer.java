package com.sfood.mb.app.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BoardSchemaCompatibilityInitializer implements ApplicationRunner {

    private final DataSource dataSource;
    private final String configuredSchema;

    public BoardSchemaCompatibilityInitializer(
            DataSource dataSource,
            @Value("${spring.jpa.properties.hibernate.default_schema:}") String configuredSchema
    ) {
        this.dataSource = dataSource;
        this.configuredSchema = configuredSchema;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            if (!"PostgreSQL".equalsIgnoreCase(metaData.getDatabaseProductName())) {
                return;
            }

            String schema = resolveSchema(connection);
            if (!tableExists(metaData, schema, "boards")) {
                return;
            }

            boolean hasParentBoardId = columnExists(metaData, schema, "boards", "parent_board_id");
            boolean hasSortOrder = columnExists(metaData, schema, "boards", "sort_order");

            if (hasParentBoardId && hasSortOrder) {
                return;
            }

            String qualifiedTable = qualify(schema, "boards");
            try (Statement statement = connection.createStatement()) {
                if (!hasParentBoardId) {
                    statement.execute("ALTER TABLE " + qualifiedTable + " ADD COLUMN IF NOT EXISTS parent_board_id VARCHAR(120)");
                    log.info("Added missing column: {}.parent_board_id", schema);
                }
                if (!hasSortOrder) {
                    statement.execute("ALTER TABLE " + qualifiedTable + " ADD COLUMN IF NOT EXISTS sort_order INTEGER NOT NULL DEFAULT 0");
                    log.info("Added missing column: {}.sort_order", schema);
                }
            }
        }
    }

    private String resolveSchema(Connection connection) throws Exception {
        if (configuredSchema != null && !configuredSchema.isBlank()) {
            return configuredSchema;
        }
        String schema = connection.getSchema();
        return (schema == null || schema.isBlank()) ? "public" : schema;
    }

    private boolean tableExists(DatabaseMetaData metaData, String schema, String table) throws Exception {
        try (ResultSet rs = metaData.getTables(null, schema, table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private boolean columnExists(DatabaseMetaData metaData, String schema, String table, String column) throws Exception {
        try (ResultSet rs = metaData.getColumns(null, schema, table, column)) {
            return rs.next();
        }
    }

    private String qualify(String schema, String table) {
        return schema + "." + table;
    }
}
