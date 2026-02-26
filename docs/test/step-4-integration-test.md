# Step 4 테스트 문서: 통합/운영 설정

## 테스트 대상
- 프로파일 분리 설정 (`application.properties`, `application-local.properties`, `application-dev.properties`)
- 전체 빌드/테스트 파이프라인

## 테스트 항목
- 기본 프로파일이 `local`인지 확인
- `local` 프로파일에서 전체 테스트 통과
- 전체 빌드 성공

## 실행 명령
- `./gradlew test`
- `./gradlew build`

## 기대 결과
- 전체 테스트 통과
- 전체 빌드 성공

## 실행 결과
- 실행 일시: 2026-02-25
- 명령: `./gradlew test`
- 결과: `BUILD SUCCESSFUL`
- 명령: `./gradlew build`
- 결과: `BUILD SUCCESSFUL`
- 비고: 기본 프로파일 `local` 기준에서 전체 회귀/빌드 통과
