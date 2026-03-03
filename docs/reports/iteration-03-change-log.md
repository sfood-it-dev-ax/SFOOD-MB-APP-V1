# Iteration 03 Change Log

## Config
- Updated `src/main/resources/application-postgres.properties`
  - default datasource URL changed to schema-based PostgreSQL URL.

## Test Artifacts
- Added `docs/plan/iteration/iteration-03-plan.md`
- Added `docs/test/results/iteration-03-test-result.md`
- Added `docs/reports/iteration-03-result.md`
- Added `docs/reports/iteration-03-change-log.md`
- Updated `docs/test/results/iteration-02-live-api-log.md` formatting

## Runtime Fix History
- Initial DB URL used `.../memo_board_v3` as database and failed.
- Switched to `.../postgres?currentSchema=memo_board_v3`, then boot and full API live test passed.
