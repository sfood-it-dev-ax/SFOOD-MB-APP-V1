# SCRAM Password 오류 재테스트 결과

기록 일시: 2026-02-25

## 목적
오류 메시지
- `The server requested SCRAM-based authentication, but no password was provided.`

가 실제로 재현되는지, 수정 후 해소되는지 검증.

## 환경
- Profile: `local`
- DB: Supabase PostgreSQL (pooler)
- 앱 설정: `src/main/resources/application-local.properties`

## 실행 1: 빈 비밀번호 인자 강제 전달 (재현 조건)
명령:
```bash
./gradlew bootRun --args='--spring.profiles.active=local --spring.datasource.password='
```
(테스트에서는 `timeout`으로 강제 종료)

결과:
- `HikariPool-1 - Start completed`
- `Tomcat started on port 8080`
- `Started SfoodMbAppV1Application`
- SCRAM 오류 미발생

판정: **통과**

## 실행 2: 일반 local 실행
명령:
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

결과:
- DB 연결/앱 기동 성공 로그 확인
- 병렬 실행 중 한 번은 `Port 8080 was already in use` 발생
  - 이는 SCRAM 오류가 아닌 포트 충돌 이슈

판정: **SCRAM 관점 통과**, 포트 충돌 주의

## 실행 3: 포트 충돌 제거 후 빈 비밀번호 + debug 옵션
명령:
```bash
./gradlew bootRun --args='--spring.profiles.active=local --spring.datasource.password= --server.port=18081 --debug'
```

결과:
- `HikariPool-1 - Start completed`
- `Tomcat started on port 18081`
- `Started SfoodMbAppV1Application`
- SCRAM 오류 미발생

판정: **통과**

## 결론
- 저장소 코드 기준으로는 SCRAM 오류가 재현되지 않음.
- 동일 오류가 계속 난다면, 실행 환경(IDE Run/Debug 설정)에서 아래를 확인 필요:

1. VM/Program args에 `--spring.datasource.password=` 가 고정되어 있는지
2. Environment variables에 `SPRING_DATASOURCE_PASSWORD` 또는 `LOCAL_DB_PASSWORD`가 빈값으로 들어있는지
3. Active profile이 `local`이 맞는지
4. 포트 충돌(`8080`)로 다른 실패를 SCRAM으로 오인하지 않는지

## 관련 수정
- `DbPasswordFallbackEnvironmentPostProcessor` 추가
- `META-INF/spring.factories` 등록
- local/dev datasource password/fallback 설정 정리
