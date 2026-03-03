# Iteration 09 Plan

## Goal
Fix board move API internal error by resolving board hierarchy column/schema mismatch in PostgreSQL runtime.

## Scope
- Explicitly map board hierarchy columns (`parent_board_id`, `sort_order`) in entity.
- Add startup schema compatibility initializer for PostgreSQL to ensure required board move columns exist.
- Keep move API contract unchanged.
- Run `./gradlew test` and `./gradlew build`.
- Document root cause, fix, and verification results.
