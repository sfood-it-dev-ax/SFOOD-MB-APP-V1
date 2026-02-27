# Iteration 07 Analysis

## Objective
Automate PostgreSQL schema provisioning with Flyway so production-like deployments can start with deterministic DB/session schema.

## Scope
- Add Flyway dependency.
- Enable Flyway only in `postgres` profile.
- Add migration scripts for:
  - core domain tables (`mb_users`, `mb_boards`, `mb_memo_types`, `mb_memos`)
  - Spring Session tables (`SPRING_SESSION`, `SPRING_SESSION_ATTRIBUTES`)
  - baseline memo type seed data.

## Migration Order
- V1: core tables + constraints.
- V2: session tables + indexes.

## Risks
- Session table DDL mismatch with Spring Session version.
- Existing initialized DBs may conflict with create-table statements.

## Mitigation
- Use `IF NOT EXISTS` in table/index creation.
- Keep local profile Flyway disabled to avoid test instability.
