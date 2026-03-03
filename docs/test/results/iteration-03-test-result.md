# Iteration 03 Test Result

## Environment
- Date: 2026-02-27 (KST)
- Profile: `postgres`
- DB URL (effective): `jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require&currentSchema=memo_board_v3`

## Executed Commands
1. `SPRING_PROFILES_ACTIVE=postgres DB_URL=jdbc:postgresql://.../memo_board_v3?sslmode=require ... ./gradlew bootRun`
- Result: FAIL
- Error: `FATAL: database "memo_board_v3" does not exist`

2. `SPRING_PROFILES_ACTIVE=postgres DB_URL=jdbc:postgresql://.../postgres?sslmode=require&currentSchema=memo_board_v3 ... ./gradlew bootRun`
- Result: PASS (application started)
- Evidence: Hikari connected, Tomcat started on `8080`.

3. Live API E2E script execution (curl-based)
- Result: PASS
- Detailed per-endpoint log: `docs/test/results/iteration-02-live-api-log.md`

4. `./gradlew test`
- Result: PASS
- Purpose: regression check after postgres profile default URL fix.

## API Scenario Coverage (Live)
- `GET /api/auth/me` (unauthorized before login)
- `POST /api/auth/login`
- `GET /api/auth/me` (authorized)
- `GET /api/memo-types`
- `POST /api/boards`
- `GET /api/boards`
- `PATCH /api/boards/{boardId}/name`
- `POST /api/memos`
- `GET /api/memos?boardId=...`
- `PATCH /api/memos/{memoId}`
- `DELETE /api/memos/{memoId}`
- `DELETE /api/boards/{boardId}`
- `POST /api/auth/logout`
- `GET /api/auth/me` (unauthorized after logout)

## Fix Applied During Test
- Updated `application-postgres.properties` default URL to schema-based form:
  - from: `jdbc:postgresql://localhost:5432/sfoodmb`
  - to: `jdbc:postgresql://localhost:5432/postgres?currentSchema=memo_board_v3`
