# Iteration 01 Test Result

## Executed Commands
1. `./gradlew test`
- Result: PASS
- Notes: All unit/integration tests completed successfully.

2. `./gradlew build`
- Result: PASS
- Notes: Build succeeded after test pass, including `bootJar` generation.

## Initial Failure and Fix
- Initial `./gradlew test` failed due to missing `DataIntegrityViolationException` class on classpath.
- Fix applied: add `implementation 'org.springframework:spring-tx'` in `build.gradle`.
- Re-run result: PASS.

## Covered Scenarios
- Auth: login/logout/me with session behavior.
- Board: create/list/update-name/delete (logical delete).
- Memo: create/list/update(content/position/size/z-index)/delete (logical delete).
- Memo type: list.
