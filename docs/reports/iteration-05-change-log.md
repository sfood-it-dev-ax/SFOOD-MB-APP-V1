# Iteration 05 Change Log

## Code
- Updated `src/main/java/com/sfood/mb/app/infrastructure/repository/impl/jpa/JpaMemoTypeRepository.java`
  - switched from static in-memory list to DB-backed reads.
- Added `src/main/java/com/sfood/mb/app/config/MemoTypeDataInitializer.java`
  - inserts default memo types (3 rows) when table is empty at app startup.

## Config
- Updated `src/main/resources/application-postgres.properties`
  - datasource default URL now includes `preferQueryMode=simple`.

## Docs
- Added
  - `docs/plan/iteration/iteration-05-plan.md`
  - `docs/test/results/iteration-05-db-check.md`
  - `docs/test/results/iteration-05-live-api-log.md`
  - `docs/test/results/iteration-05-test-result.md`
  - `docs/reports/iteration-05-result.md`
  - `docs/reports/iteration-05-change-log.md`
