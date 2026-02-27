# Front Test 01 Troubleshooting

## Scope
- Reproduce and diagnose the reported 500 responses on:
  - `POST /api/v1/boards/{boardId}/memos`
  - `GET /api/v1/boards/{boardId}/memos`
  - `PATCH /api/v1/memos/{memoId}/content`
  - `PATCH /api/v1/memos/{memoId}/position`
  - `PATCH /api/v1/memos/{memoId}/size`
  - `PATCH /api/v1/boards/{boardId}/memos/zindex`
  - `DELETE /api/v1/memos/{memoId}`
  - `DELETE /api/v1/boards/{boardId}`

## Reproduction and Findings
1. Added dedicated regression test:
- `src/test/java/com/sfood/mb/app/FrontIssue01RegressionTest.java`
- Uses front-style IDs (`b_<timestamp>_e2e`, `<boardId>_m1`) and executes the same endpoint order.

2. First reproduction run:
- Login failed with `401 UNAUTHORIZED (Google token mismatch)` for a previously persisted user token.
- This confirms that token persistence verification is active and can break replay attempts using changed token values.

3. Stable reproduction run (unique user token each run):
- All reported endpoints executed without 5xx.
- `./gradlew test --tests com.sfood.mb.app.FrontIssue01RegressionTest` passed.

## Root Cause(s)
- Primary observed cause during troubleshooting:
  - Replayed login token for an existing user did not match stored token, causing auth failure (`401`).
- Secondary platform issue that masked diagnosis:
  - Unhandled infrastructure/request-shape exceptions were returned as generic `500 INTERNAL_ERROR`, hiding actionable cause details.

## Fixes Applied
1. Request/response logging for all API calls:
- `ApiRequestLoggingFilter` added and wired in security filter chain.
- Logs now show:
  - `API REQ - METHOD URI (sessionId=...)`
  - `API RES - METHOD URI -> STATUS (ms)`

2. Exception observability + error mapping hardening:
- `GlobalExceptionHandler` now logs request URI/method and full stack trace for unhandled errors.
- Added explicit handlers:
  - `HttpMessageNotReadableException` -> `400 VALIDATION_ERROR`
  - `DataIntegrityViolationException` -> `409 CONFLICT`
- This prevents many previously generic 500 responses for malformed/conflicting input.

## Validation
- `./gradlew test --tests com.sfood.mb.app.FrontIssue01RegressionTest` -> PASS
- `./gradlew test` -> PASS
- `./gradlew build` -> PASS

## Operational Checkpoint for Front
- If a request is sent but backend seems silent:
  - Confirm `API REQ` log exists for that URI.
  - If no `API REQ`, request did not reach this backend instance (base URL/proxy/port mismatch).
  - If `API REQ` exists, inspect paired `API RES` status and `GlobalExceptionHandler` log line.
