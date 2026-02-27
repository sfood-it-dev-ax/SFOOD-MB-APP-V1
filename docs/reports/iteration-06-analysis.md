# Iteration 06 Analysis

## Objective
Introduce profile-based datasource/session configuration so local testing remains H2-based while production-like deployment can use PostgreSQL safely.

## Scope
- Move existing H2 datasource config into `application-local.properties`.
- Keep common settings in `application.properties`.
- Add `application-postgres.properties` for PostgreSQL + Session JDBC.
- Activate `local` profile by default.

## Config Matrix
- local profile:
  - H2 datasource
  - JPA ddl-auto update
  - Session JDBC schema init always
- postgres profile:
  - PostgreSQL datasource via env placeholders
  - JPA ddl-auto validate
  - Session JDBC schema init never

## Risks
- Missing env vars in postgres profile can break startup.
- Session table absence in postgres can break auth flow.

## Mitigation
- Use explicit `${ENV_VAR:}` placeholders.
- Document requirement for session schema provisioning in postgres runtime.
