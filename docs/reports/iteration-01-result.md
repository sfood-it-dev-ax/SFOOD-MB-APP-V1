# Iteration 01 Result

## 작성일
- 2026-03-03

## 결과 요약
- PRD 기준 API(Auth/Board/Memo/Memo Types)를 구현했고, Supabase PostgreSQL에 실제 데이터를 전송해 통합 테스트를 완료했다.
- 테스트 완료 결과를 API Spec 및 Test Result 문서에 반영했다.

## 구현 범위
- 세션 인증: `POST /api/v1/auth/login`, `POST /api/v1/auth/logout`, `GET /api/v1/auth/me`
- 보드: `GET/POST /api/v1/boards`, `PATCH /api/v1/boards/{boardId}/name`, `PATCH /api/v1/boards/{boardId}/move`, `DELETE /api/v1/boards/{boardId}`
- 메모: `GET/POST /api/v1/boards/{boardId}/memos`, `PATCH /api/v1/memos/{memoId}/content`, `PATCH /api/v1/memos/{memoId}/position`, `PATCH /api/v1/memos/{memoId}/size`, `PATCH /api/v1/boards/{boardId}/memos/zindex`, `DELETE /api/v1/memos/{memoId}`
- 메모 타입: `GET /api/v1/memo-types`

## 핵심 결정
- 로그인 검증 정책: 외부 Google API 검증 없이 `googleAccount`/`token` 존재 및 사용자 존재 여부 기반 처리
- 삭제 정책: 보드/메모 모두 논리 삭제(`is_hide=true`)
- Supabase PgBouncer 호환을 위해 JDBC URL 옵션(`prepareThreshold=0`, `preferQueryMode=simple`) 적용

## 검증 결과
- `./gradlew test --tests ApiPostgresqlIntegrationTest`: PASS
- `./gradlew test`: PASS

## 산출 문서
- `docs/product/prd.md`
- `docs/product/api-spec.md`
- `docs/test/results/iteration-01-test-result.md`
- `docs/reports/iteration-01-result.md`

## 남은 과제
- Frontend 연동 시 자동 저장(1초 딜레이) UX 동작 검증
- 운영/개발 환경별 Supabase schema 분리 정책 확정
