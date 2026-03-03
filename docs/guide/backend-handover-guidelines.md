# Backend Handover Guidelines (Front-Test 01 Reflection)

## 1) 목적
- 이번 백엔드 개발/디버깅 과정에서 실제로 발생한 문제와 수정 내역을 코드/문서 기준으로 정리한다.
- 다음 개발에서 동일 문제를 재발시키지 않기 위한 운영 지침을 제안한다.

## 2) 코드-문서 비교 분석

### 2.1 문서에 반영된 주요 변경
- `docs/product/api-spec.md`
  - `CONFLICT(409)` 에러 코드 가이드 추가
  - 백엔드 API 수신/응답 로그(`API REQ`, `API RES`) 확인 규칙 추가
  - Google token replay mismatch(`401`) 규칙 명시
- `docs/issue/front-test-01-result.md`
  - 세션 테이블 스키마 인식 문제와 대응 내역 정리
- `docs/issue/front-test-01-troubleshooting.md`
  - 재현 테스트 기반 원인 추적, 보강된 예외 처리, 검증 결과 정리

### 2.2 코드에 반영된 주요 변경
- 세션 스키마/테이블 명시:
  - `spring.session.jdbc.table-name=memo_board.SPRING_SESSION`
  - postgres profile의 `search_path/default_schema/flyway schema` 정렬
- API 관측성 강화:
  - 컨트롤러 진입 로그 (`API IN - ...`)
  - `ApiRequestLoggingFilter`로 `/api/**` 요청/응답 로그 (`API REQ/RES`)
  - `logging.level.com.sfood.mb.app=INFO` 기본 설정
- 예외 처리 개선:
  - `GlobalExceptionHandler`에 URI/메서드 로그 추가
  - `HttpMessageNotReadableException -> 400 VALIDATION_ERROR`
  - `DataIntegrityViolationException -> 409 CONFLICT`
- 경로 변수 바인딩 안정화:
  - 모든 `@PathVariable`에 이름 명시
  - `build.gradle`에 `-parameters` 컴파일 옵션 추가
- 재현 테스트 추가:
  - `FrontIssue01RegressionTest` (프론트 보고 순서 기반 E2E 흐름)

## 3) 실제 발생 오류와 수정 포인트

### 3.1 Spring Session 테이블 스키마 불일치
- 증상: `BadSqlGrammarException`, `relation "spring_session" does not exist`
- 원인: 세션 쿼리가 schema-unqualified 테이블을 조회
- 조치: `memo_board.SPRING_SESSION` 명시 + profile별 schema/search_path 일치화

### 3.2 "요청 보냈는데 백엔드 로그 없음"
- 증상: 프론트에서는 요청 송신, 서버에서 진입 확인 불가
- 원인: 컨트롤러/필터 레벨 요청 로그 부재 + 로그 레벨 환경 의존
- 조치: `/api/**` 공통 요청/응답 로그 필터 도입, 패키지 INFO 레벨 명시

### 3.3 동일 유저 재로그인 시 `401 Google token mismatch`
- 증상: 테스트/수동 호출에서 로그인부터 실패
- 원인: 동일 userId에 저장된 `googleToken`과 다른 토큰으로 재로그인
- 조치: 재현 테스트는 매 실행 고유 사용자 토큰 사용, 스펙에 규칙 명시

### 3.4 `@PathVariable` 이름 추론 실패로 500
- 증상: `Name for argument of type [java.lang.String] not specified...`
- 원인: 런타임/IDE 환경에서 `-parameters` 부재 시 이름 추론 실패
- 조치: `@PathVariable("...")` 전면 명시 + 컴파일러 `-parameters` 강제

### 3.5 IDE 실행은 되는데 CLI 빌드 실패
- 증상: IDE Run/Debug는 동작, `./gradlew build` 실패
- 원인: 증분 컴파일/실행 환경(프로필, JDK, env, classpath) 불일치
- 조치: CLI 기준 빌드/테스트를 진실 소스로 두고, IDE 실행 설정을 동기화

## 4) 다음 개발에 추가할 필수 지침 (제안)

### 4.1 개발 규칙(`development-rules.md`)에 추가
- `@PathVariable` / `@RequestParam`는 이름을 항상 명시한다.
- Spring JDBC Session 사용 시 테이블 이름은 schema-qualified로 설정한다.
- `/api/**` 요청/응답 로그는 공통 필터에서 남긴다.
- `GlobalExceptionHandler`에서 인프라/요청 형식 예외를 4xx로 매핑한다.

### 4.2 테스트 전략(`testing-strategy.md`)에 추가
- 프론트 이슈 재현용 회귀 테스트를 유지한다 (`FrontIssue01RegressionTest` 계열).
- 로그인 검증 테스트는 고유 user token을 사용해 replay mismatch를 회피한다.
- 검증 명령은 병렬 실행하지 않는다:
  - `./gradlew test`
  - `./gradlew build`
  (동일 `build/test-results` 경합 방지)

### 4.3 완료 기준(`done-criteria.md`)에 추가
- 필수 관측성 체크:
  - `/api/**` 호출 시 `API REQ`/`API RES` 로그 존재
  - 예외 발생 시 URI 포함 로그 존재
- 필수 환경 동기화 체크:
  - `spring.profiles.active`, JDK, env vars, base URL/port 문서화
- API 에러 계약 체크:
  - `400/401/404/409` 분기와 메시지 문서 일치

## 5) 운영 체크리스트 (재사용용)
- 1. `./gradlew test` 성공
- 2. `./gradlew build` 성공
- 3. `/api/**` 호출 시 `API REQ`와 `API RES` 로그 확인
- 4. 5xx 발생 시 `GlobalExceptionHandler`의 `Unhandled exception` stacktrace 확인
- 5. 프론트 로그인 실패 시 먼저 `Google token mismatch(401)` 여부 확인
- 6. IDE/CLI 불일치 시 프로필/JDK/env/classpath를 먼저 비교

## 6) 결론
- 이번 이슈는 단일 버그보다 "환경 차이 + 관측성 부족 + 예외 매핑 부족"이 결합된 사례였다.
- 다음 개발부터는 경로 바인딩 명시, 세션 스키마 명시, 공통 API 로그, 4xx 예외 매핑, 재현 테스트 유지를 기본 규칙으로 적용해야 한다.
