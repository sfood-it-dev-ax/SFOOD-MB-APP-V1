# Iteration 09 Result

## Delivered Scope
- `local` profile now connects to Supabase PostgreSQL using `postgres` DB and `memo_board` schema.
- Flyway compatibility for PostgreSQL 17.6 added.
- Flyway schema baseline/migration setup applied for existing non-empty schema.
- Supabase pooler-safe JDBC options applied.
- Domain entity column mappings aligned to snake_case schema columns.
- Live API calls confirmed data creation in Supabase-backed flow.

## Key Changes
- `build.gradle`
  - added `org.flywaydb:flyway-database-postgresql`
- `src/main/resources/application-local.properties`
  - changed JDBC DB target to `postgres`
  - added schema/search_path/flyway baseline and pooler options
- `src/main/resources/db/migration/postgres/V1__create_core_tables.sql`
  - added schema init/search_path
- `src/main/resources/db/migration/postgres/V2__create_spring_session_tables.sql`
  - added schema init/search_path
- Domain mappings updated:
  - `Board`, `User`, `Memo`, `MemoType` column names explicitly mapped to DB columns
- `ApiFlowIntegrationTest`
  - switched to unique IDs per run to avoid real DB data collision

## Validation Summary
- App debug startup (`local`): PASS
- API flow (login/board/memo create/list): PASS
- `./gradlew test`: PASS
- `./gradlew build`: PASS

## Notes
- `memo_board` is handled as schema, not database name.
- Frontend can now proceed using session cookie-based auth and current REST endpoints.
