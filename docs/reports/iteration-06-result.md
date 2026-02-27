# Iteration 06 Result

## Delivered Scope
- Profile-based 설정 분리 적용:
  - 공통: `application.properties`
  - 로컬(H2): `application-local.properties`
  - 운영유사(PostgreSQL): `application-postgres.properties`
- 기본 프로파일을 `local`로 설정 (`spring.profiles.default=local`).
- PostgreSQL 런타임 드라이버 의존성 추가 (`org.postgresql:postgresql`).
- Session/JPA 정책 프로파일별 분리:
  - local: `ddl-auto=update`, session schema init `always`
  - postgres: `ddl-auto=validate`, session schema init `never`

## Key Decisions
- 테스트 안정성을 위해 기본 실행은 local(H2) 유지.
- postgres 프로파일은 환경변수 기반 datasource 설정으로 외부 주입 전제.

## Remaining Gaps
- postgres 프로파일 실제 DB 연결 smoke test는 환경 변수/DB 접근정보가 필요하여 미실행.
- PostgreSQL 세션 테이블 사전 배포(DDL/migration) 자동화는 후속 이터레이션 권장.
