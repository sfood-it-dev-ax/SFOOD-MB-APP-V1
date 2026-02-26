# Step 4 분석: 통합 정리 및 운영 설정

기준 문서:
- `docs/plan/backend-development-plan.md` (Step 4)
- `docs/guide/db-spec.md`

## 1. 이번 단계 목표
- DB 프로파일(local/dev) 분리
- MySQL 운영/개발 실행 가이드 정리
- 전체 스모크 테스트 문서화

## 2. 설정 전략
- `application.properties`: 공통 설정만 유지, 기본 프로파일 `local`
- `application-local.properties`: H2 메모리 DB + 개발 편의 옵션
- `application-dev.properties`: MySQL 8.0 연결 설정 (환경변수 기반 오버라이드 가능)

## 3. 환경 변수 정책
- 민감 정보는 환경변수 우선 사용
- 기본값은 기존 명세(`db-spec.md`) 기반으로 제공

## 4. 검증 전략
- `local` 프로파일 기준 전체 빌드/테스트
- 애플리케이션 컨텍스트 로딩 및 주요 API 스모크 경로 점검

## 5. 기대 산출물
- 프로파일 분리 설정 파일
- MySQL 실행 가이드 문서
- 통합 테스트 문서(실행 명령/결과 포함)
