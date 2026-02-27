# Iteration 08 Result

## Delivered Scope
- 보안 예외 JSON 응답 핸들러 추가:
  - `JsonAuthenticationEntryPoint` (401)
  - `JsonAccessDeniedHandler` (403)
- `SecurityConfig`에 보안 예외 핸들러 연결.
- 401 응답 바디 스키마 검증 테스트 보강:
  - `SecurityUnauthorizedTest`에서 `success/errorCode/message/timestamp` 확인.

## Key Decisions
- 보안 실패도 글로벌 에러 스키마와 동일 포맷으로 통일.
- 기존 인증 정책(로그인만 permitAll, 나머지 /api/** 인증필수)은 유지.

## Remaining Gaps
- 403 케이스를 실제로 재현하는 엔드포인트/권한 모델이 아직 없어서 403 통합 테스트는 후속 필요.
