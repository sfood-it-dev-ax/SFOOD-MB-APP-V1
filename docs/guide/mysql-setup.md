# MySQL 실행 가이드 (Spring Profile: dev)

## 1) 사전 조건
- MySQL 8.0
- 데이터베이스: `memo_board`
- 계정: `APP_DEV`

## 2) 환경 변수 설정 (권장)
```bash
export MEMO_DB_URL="jdbc:mysql://localhost:3306/memo_board?serverTimezone=Asia/Seoul&characterEncoding=UTF-8"
export MEMO_DB_USERNAME="APP_DEV"
export MEMO_DB_PASSWORD="sfoodOMSdev2024!!!"
```

## 3) 애플리케이션 실행
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 4) 테스트 실행
- 로컬 기본 프로파일(`local`)에서 테스트:
```bash
./gradlew test
```
- dev 프로파일 컨텍스트 확인(선택):
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 5) 참고
- 기본 프로파일은 `local`이며 H2 메모리 DB를 사용한다.
- `dev`는 MySQL 연결 프로파일이다.
