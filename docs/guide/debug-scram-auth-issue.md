# Debug 실행 시 SCRAM 인증 오류 대응 가이드

기록일: 2026-02-25

## 1) 증상
디버깅/런 설정에서 애플리케이션 시작 시 아래 오류가 발생함.

- `The server requested SCRAM-based authentication, but no password was provided.`
- `Unable to open JDBC Connection for DDL execution`

## 2) 원인
실행 환경(특히 IDE Debug Run Configuration)에서 `spring.datasource.password`가 빈 문자열로 전달되면,
설정 파일의 비밀번호 값이 덮어써져 PostgreSQL SCRAM 인증이 실패함.

예시 재현:
```bash
./gradlew bootRun --args='--spring.profiles.active=local --spring.datasource.password='
```

## 3) 해결 내용
다음 두 가지를 적용함.

1. `application-local.properties`에 local DB 비밀번호를 명시
2. `DbPasswordFallbackEnvironmentPostProcessor` 추가
   - `spring.datasource.password`가 비어 있으면 fallback 비밀번호를 강제로 주입
   - IDE/디버그 런에서 빈 비밀번호를 넘겨도 접속 실패를 방지

## 4) 수정 파일
- `src/main/java/com/sfood/mb/app/config/DbPasswordFallbackEnvironmentPostProcessor.java`
- `src/main/resources/META-INF/spring.factories`
- `src/main/resources/application-local.properties`
- `src/main/resources/application-dev.properties`

## 5) 검증 결과
재현 명령으로 테스트:
```bash
./gradlew bootRun --args='--spring.profiles.active=local --spring.datasource.password='
```

결과:
- DB 연결 성공 (`HikariPool-1 - Start completed`)
- 애플리케이션 기동 성공 (`Started SfoodMbAppV1Application`)

## 6) 운영 팁
- IDE Run/Debug Configuration에서 `spring.datasource.password`를 빈 값으로 전달하지 않도록 확인
- 디버그 JVM 대기(`--debug-jvm`)는 앱 오류가 아니라 디버거 연결 대기 상태일 수 있음
