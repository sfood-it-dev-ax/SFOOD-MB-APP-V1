# Iteration 09 Test Result

## Commands
1. `./gradlew test`
- Result: PASS

2. `./gradlew build`
- Result: PASS

## Verification
- Board move regression path remains covered by integration flow tests.
- Explicit board column mapping compiles and passes all existing tests.
- PostgreSQL startup compatibility initializer compiles and is included in application context.

## Notes
- Local CLI environment could not directly run PostgreSQL at `localhost:5432`, so production-like live move retest must be done on the running backend after deploy/restart.

## Live API Retest
1. Existing running server (`http://localhost:8080`)
- Flow: `login -> create board -> move -> list`
- Result: move failed (`500 INTERNAL_ERROR`)
- Observation: response payload shape matched old board response (no `parentBoardId`, `sortOrder`), indicating old runtime binary was still serving.

2. Updated code runtime (`http://localhost:8082`, local `bootRun`)
- Flow: `login -> create board -> move -> list -> cleanup`
- Result: move succeeded (`200`)
- Response confirmed:
  - `parentBoardId: null`
  - `sortOrder: 5`
