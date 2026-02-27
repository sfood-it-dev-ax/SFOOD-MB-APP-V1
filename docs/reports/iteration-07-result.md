# Iteration 07 Result

## Delivered Scope
- Flyway 의존성 추가 (`org.flywaydb:flyway-core`).
- 프로파일별 Flyway 정책 설정:
  - local: `spring.flyway.enabled=false`
  - postgres: `spring.flyway.enabled=true`, `spring.flyway.locations=classpath:db/migration/postgres`
- PostgreSQL 마이그레이션 스크립트 추가:
  - `V1__create_core_tables.sql` (core 테이블 + memo type seed)
  - `V2__create_spring_session_tables.sql` (Spring Session 테이블/인덱스)

## Key Decisions
- local 테스트 안정성을 위해 Flyway는 local에서 비활성 유지.
- postgres 실행 시에만 Flyway로 스키마/세션 테이블 자동화.

## Remaining Gaps
- postgres 프로파일 실DB 대상 migration 실행 smoke test는 환경변수/DB 접속 정보 필요.
- 운영 전 migration 버전 관리 정책(롤백/핫픽스 규칙) 문서화 권장.
