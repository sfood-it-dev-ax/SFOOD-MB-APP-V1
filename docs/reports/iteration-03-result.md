# Iteration 03 Result

## Delivered Scope
- `spring-boot-starter-security` 의존성 추가.
- `SecurityConfig` 도입:
  - `POST /api/v1/auth/login` 허용
  - 그 외 `/api/**` 인증 필수
  - CSRF 비활성(API 서버 기준)
  - 인증 실패 시 401 응답
- `SessionAuthenticationFilter` 추가:
  - HttpSession의 `LOGIN_USER_ID`를 SecurityContext 인증 객체로 매핑.
- `SessionUserResolver` 추가 및 컨트롤러 리팩터링:
  - 인증 사용자 ID 조회 로직 공통화.

## Validation Notes
- 초기 테스트 실패(인증 컨텍스트 미연결) 후 필터 보완으로 해결.
- `./gradlew test`, `./gradlew build` 모두 최종 통과.

## Remaining Gaps
- Google OAuth 토큰 실제 검증 로직은 아직 미구현(현재 mock parsing).
- Spring Session JDBC 저장소와 세션 하이재킹 방어 옵션 고도화 필요.
