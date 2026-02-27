# Iteration 09 Analysis

## Request Summary
- Run backend in debug mode.
- Use `local` profile to connect to Supabase PostgreSQL (not local DB instance).
- Validate table creation and API-driven data creation.

## Reference
- `docs/guide/architectures.md` defines PostgreSQL endpoint:
  - host: `aws-1-ap-south-1.pooler.supabase.com`
  - port: `6543`
  - schema/database: `memo_board`

## Target Behavior
1. `local` profile starts with PostgreSQL driver and Supabase JDBC URL.
2. Flyway migration runs on startup and creates required tables.
3. Session-based auth login works and issues `JSESSIONID`.
4. API calls create persisted board/memo records.

## Planned Verification
- Startup command:
  - `./gradlew bootRun --args='--spring.profiles.active=local --debug'`
- API sequence:
  - `POST /api/v1/auth/login`
  - `GET /api/v1/auth/me`
  - `POST /api/v1/boards`
  - `POST /api/v1/boards/{boardId}/memos`
  - `GET /api/v1/boards`
  - `GET /api/v1/boards/{boardId}/memos`

## Risks
- Network-restricted environment may block Supabase access.
- External DB credentials/connectivity issues can fail migration/startup.
