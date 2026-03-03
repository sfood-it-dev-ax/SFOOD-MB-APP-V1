# Testing Strategy

## Test Types
- Unit tests for service business logic.
- Web MVC tests for controller contract and status codes.
- Integration-style context load test.
- Front-issue regression tests for previously failed API flows (keep reproducible endpoint sequence).

## Minimum Coverage for Iteration 01
- Auth login/logout/me flow (session behavior).
- Board create/list/update-name/delete.
- Memo create/list/update-content/position/size/z-index/delete.
- Memo type list.

## Execution Rules
- Run `./gradlew test` for code changes.
- Run `./gradlew build` when wiring/config/dependencies change.
- Record commands and outcomes in iteration test result document.
- Run verification commands sequentially (avoid parallel `test`/`build` execution that can race on `build/test-results` files).
- For auth tests tied to persisted token verification, use unique test users/tokens per run unless mismatch behavior is the target scenario.
