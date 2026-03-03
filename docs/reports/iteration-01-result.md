# Iteration 01 Result

## Delivered Scope
- Implemented session-based backend APIs for auth, board, memo, memo type.
- Applied interface-first service design and separated controller/service/repository/domain responsibilities.
- Added centralized error handler with consistent error payload.
- Added `/api/**` request/response logging filter.
- Added tests for core flows and logical delete behavior.

## Key Decisions
- Used in-memory repositories for Iteration 01 to deliver executable API behavior without DB coupling.
- Enforced logical delete (`isHide`) for board/memo deletion.
- Added default board creation on first login.

## Remaining Gaps
- Google OAuth integration is not implemented.
- Persistent DB/JPA integration is not implemented.
- Board hierarchy move API is not implemented.

## Artifacts
- Plan: `docs/plan/iteration/iteration-01-plan.md`
- Test result: `docs/test/results/iteration-01-test-result.md`
- Change log: `docs/reports/iteration-01-change-log.md`
