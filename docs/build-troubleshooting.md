# Build 이슈 점검 이력

기록 일자: 2026-02-25
프로젝트: `SFOOD-MB-APP-V1`

## 1) 최초 증상
- `gradle build` 또는 `./gradlew build` 실행 시 빌드 실패
- IntelliJ/WSL 환경에서 다음 메시지 확인:
  - 프로젝트가 저장된 WSL 배포판과 빌드용 JDK 위치가 다름
  - 빌드 실행용 JDK는 프로젝트와 같은 WSL 배포판 내부에 있어야 함
  - 빌드 실행에는 JDK 11+ 필요

## 2) 확인된 원인
- 원인 A: `gradlew` 실행 권한 누락
  - `./gradlew` 실행 시 `Permission denied` 발생
- 원인 B: IDE의 Gradle JVM 설정 불일치 (WSL JDK 미사용)
  - 프로젝트는 WSL(Ubuntu)에 있으나, 빌드 JDK가 동일 배포판 내부 경로가 아님

## 3) 처리 이력
1. 빌드 재현 시도
   - `./gradlew build --stacktrace`
   - 결과: `Permission denied`
2. 우회 실행으로 추가 점검
   - `bash gradlew build --stacktrace`
   - 결과: Wrapper/환경 접근 이슈 확인 (실행 환경 제약)
3. 실행 권한 복구
   - `chmod +x gradlew`
   - 결과: `./gradlew` 직접 실행 가능
4. 빌드 정상 여부 확인
   - `./gradlew build`
   - 결과: `BUILD SUCCESSFUL` 확인 (CLI 기준)
5. IDE 오류 원인 정리
   - WSL 프로젝트는 WSL 내부 JDK를 Gradle JVM으로 사용해야 함

## 4) 최종 해결 방법
1. WSL Ubuntu에 JDK 21 설치
   - `sudo apt update`
   - `sudo apt install -y openjdk-21-jdk`
   - `java -version`으로 확인
2. IntelliJ에 WSL JDK 등록
   - `File > Project Structure > SDKs`
   - 예시 경로: `/usr/lib/jvm/java-21-openjdk-amd64`
3. Gradle JVM을 동일 WSL JDK로 지정
   - `Settings > Build, Execution, Deployment > Build Tools > Gradle`
   - `Gradle JVM` = WSL JDK 21
4. 프로젝트 빌드 확인
   - `./gradlew -version`
   - `./gradlew build`

## 5) 재발 방지 체크리스트
- 저장소 클론 직후 `gradlew` 실행 권한 확인
  - `ls -l gradlew` 에서 `x` 권한 포함 여부 확인
- WSL 프로젝트는 반드시 WSL 내부 JDK를 사용하도록 IDE/Gradle JVM 설정
- JDK 버전은 프로젝트 설정(`build.gradle`의 Java toolchain 21)과 일치시키기
