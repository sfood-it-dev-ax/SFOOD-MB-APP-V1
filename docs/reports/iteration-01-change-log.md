# Iteration 01 Change Log

## Documentation
- Added `docs/product/prd.md`
- Added `docs/product/data-spec.md`
- Added `docs/plan/iteration/iteration-01-plan.md`
- Added `docs/test/results/iteration-01-test-result.md`
- Added `docs/reports/iteration-01-result.md`
- Added `docs/reports/iteration-01-change-log.md`

## Backend Code
- Added API envelope and error contracts.
- Added global exception handling for `400/401/404/409/500` cases.
- Added `/api/**` logging filter.
- Added interface-first service contracts and implementations.
- Added in-memory repositories for users/boards/memos/memo-types.
- Added auth/board/memo controllers and DTOs.
- Added session timeout and package logging config.
- Updated dependencies for validation and Spring TX.

## Tests
- Added unit tests:
  - `BoardServiceImplTest`
  - `MemoServiceImplTest`
- Added integration flow test:
  - `ApiFlowIntegrationTest`
