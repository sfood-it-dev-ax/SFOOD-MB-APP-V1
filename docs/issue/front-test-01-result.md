# Front Test 01 Result

## Issue Summary
- Front API 테스트에서 다수 엔드포인트가 `500 INTERNAL_ERROR`로 실패.
- Front에서 API를 보냈지만 backend 로그에서 컨트롤러 진입 여부를 확인하기 어려움.

## Root Cause
- Spring Session JDBC가 `SPRING_SESSION` 테이블을 unqualified name으로 조회하면서, PostgreSQL `memo_board` 스키마를 찾지 못해 예외가 발생.
- 예외가 글로벌 핸들러에서 `Unexpected error occurred`로 응답되어 프론트에서는 상세 원인 파악이 어려웠음.
- 컨트롤러에 요청 수신 로그가 없어서 "요청이 서버까지 도달했는지" 즉시 판단하기 어려웠음.

## Fix Applied
1. Session schema/table resolution fix
- `spring.session.jdbc.table-name=memo_board.SPRING_SESSION` 적용.
- `application-postgres.properties`에 `search_path`, `hibernate default_schema`, `flyway schema/default-schema`를 `memo_board`로 명시.

2. Controller request-in logs
- 아래 컨트롤러의 모든 API 엔드포인트 진입 시 `INFO` 로그 추가:
  - `AuthController`
  - `BoardController`
  - `MemoController`
  - `MemoTypeController`
- 로그 예시:
  - `API IN - PATCH /api/v1/boards/{boardId}/name (sessionId=...)`
  - `API IN - POST /api/v1/boards/{boardId}/memos (memoId=..., sessionId=...)`

## Verification
- Command: `./gradlew test` -> `BUILD SUCCESSFUL`
- Command: `./gradlew build` -> `BUILD SUCCESSFUL`

## Expected Front Behavior After Fix
- Front가 `/api/v1/**`로 요청하면 서버 로그에서 `API IN - ...` 라인으로 컨트롤러 수신 여부 확인 가능.
- 기존 500 원인이었던 `SPRING_SESSION` relation 오류는 재발하지 않아야 함.
