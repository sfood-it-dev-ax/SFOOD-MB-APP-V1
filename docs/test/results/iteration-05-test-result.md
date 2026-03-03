# Iteration 05 Test Result

## Runtime Fixes
1. `memo_types` persisted seed
- Added `MemoTypeDataInitializer` to insert default 3 types when table is empty.
- `JpaMemoTypeRepository` now reads from DB table.

2. Supabase transaction pooler compatibility
- Added `preferQueryMode=simple` in postgres datasource URL default.
- Resolved runtime issue: `prepared statement "S_1" already exists`.

## Executed Validation (`memo_board_v3` only)
1. Boot
- `SPRING_PROFILES_ACTIVE=postgres`
- `DB_URL=jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/postgres?sslmode=require&preferQueryMode=simple`
- `DB_SCHEMA=memo_board_v3`
- `./gradlew bootRun`
- Result: PASS

2. DB data check
- See `docs/test/results/iteration-05-db-check.md`
- Result: PASS (`memo_types_count=3`)

3. Live API E2E
- See `docs/test/results/iteration-05-live-api-log.md`
- Result: PASS

4. Regression
- `./gradlew test`
- Result: PASS

## Verdict
- `memo_board_v3` only flow completed successfully.
- Default memo type data requirement (3~5) satisfied with 3 records.
