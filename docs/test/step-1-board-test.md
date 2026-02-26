# Step 1 테스트 문서: Board API

## 테스트 대상
- BoardService
- BoardController

## 테스트 범위
- 보드 생성 성공
- 보드 목록 조회 시 HIDE=false만 조회
- 존재하지 않는 보드 수정 시 예외
- 보드 삭제 시 논리 삭제(HIDE=true)
- 컨트롤러 입력 검증 오류 처리
- 컨트롤러 수정/삭제 API 상태 코드 검증
- 컨트롤러 테스트는 실제 DB(H2) 기준으로 수행
- 테스트 순서: 테이블 자동 생성 -> `@BeforeEach` 데이터 생성 -> 테스트 실행 -> `@AfterEach` 데이터 삭제

## 실행 명령
- `./gradlew test`

## 기대 결과
- 모든 테스트 통과
- 실패 시 원인 로그 확인 후 수정

## 실행 결과
- 실행 일시: 2026-02-25
- 명령: `GRADLE_USER_HOME=/home/donghyunlee/projects/SFOOD-MB-APP-V1/.gradle-local ./gradlew test`
- 결과: `BUILD SUCCESSFUL`
- 비고: 기존 `@WebMvcTest + Mock` 방식에서 `@SpringBootTest + MockMvc + BoardRepository` 통합 테스트 방식으로 변경
