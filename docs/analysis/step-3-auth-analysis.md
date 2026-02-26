# Step 3 분석: 인증/세션 구조 1차 구현

기준 문서:
- `docs/guide/requirement.md` (AUTH-001 ~ AUTH-011)
- `docs/plan/backend-development-plan.md` (Step 3)

## 1. 이번 단계 구현 범위
- 로그인 API
- 로그아웃 API
- 세션 확인 API
- 세션 체크 인터셉터(인증 API 보호)

## 2. 설계 방향
- Google OAuth 실제 검증은 제외하고, Step 3에서는 로그인 입력값 기반 세션 발급 골격만 제공
- 세션은 서버 메모리 저장소(`ConcurrentHashMap`)에 보관, 만료 시간은 24시간
- 세션 토큰은 요청 헤더 `X-Session-Token` 사용

## 3. API 설계
- `POST /api/v1/auth/login`
  - 입력: `userEmail`, `userName`
  - 처리: 사용자 신규 생성(없으면) + 기본 보드 생성 + 세션 발급
  - 출력: `sessionToken`, `expiresAt`, `userEmail`, `userName`, `newUser`
- `POST /api/v1/auth/logout`
  - 인증 필요(`X-Session-Token`)
  - 처리: 세션 무효화
- `GET /api/v1/auth/session`
  - 인증 필요(`X-Session-Token`)
  - 처리: 세션 유효성 확인 및 사용자 정보 반환

## 4. 데이터 모델
- `UserAccount`
  - `userEmail` (PK)
  - `userName`
  - `createdAt`, `updatedAt`

## 5. 규칙
- 신규 사용자 로그인 시 기본 보드 1개 자동 생성
- 세션 만료/미존재 시 401 응답
- 인터셉터는 우선 `/api/v1/auth/session`, `/api/v1/auth/logout`에만 적용

## 6. 검증 전략
- 서비스 테스트: 신규/기존 로그인, 세션 조회, 로그아웃
- 컨트롤러 테스트: 로그인, 세션 조회(인증), 로그아웃, 무효 토큰 401
