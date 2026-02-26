# Step 3 테스트 문서: Auth/Session API

## 테스트 대상
- AuthService
- AuthController
- SessionAuthInterceptor

## 테스트 범위
- 로그인 성공(신규 사용자 생성, 기본 보드 생성, 세션 발급)
- 기존 사용자 로그인
- 세션 조회 성공(유효 토큰)
- 세션 조회 실패(토큰 없음/무효 토큰)
- 로그아웃 후 세션 무효화

## 테스트 방식
- 컨트롤러 테스트는 실제 DB(H2) + 실제 세션 저장소 기준
- 테스트 순서: 데이터 정리 -> 로그인으로 세션 생성 -> 인증 API 호출 -> 데이터 정리

## 실행 명령
- `./gradlew test --tests "com.sfood.mb.app.auth.*"`
- `./gradlew test`

## 기대 결과
- Auth 관련 테스트 전부 통과
- 기존 Board/Memo 테스트 포함 전체 회귀 통과

## 실행 결과
- 실행 일시: 2026-02-25
- Auth 실행: `./gradlew test --tests "com.sfood.mb.app.auth.*"` 통과 (`BUILD SUCCESSFUL`)
- 회귀 실행: `./gradlew test` 통과 (`BUILD SUCCESSFUL`)
- 비고: 인터셉터는 `/api/v1/auth/session`, `/api/v1/auth/logout`에 적용됨
