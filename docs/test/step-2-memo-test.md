# Step 2 테스트 문서: Memo API

## 테스트 대상
- MemoService
- MemoController

## 테스트 범위
- 메모 생성 성공
- 보드 미존재 시 메모 생성 실패
- 메모 수정(내용/좌표/크기/z-index/스타일) 성공
- 메모 삭제 시 논리 삭제(HIDE=true)
- boardId 기준 메모 목록 조회 성공

## 테스트 방식
- 컨트롤러 테스트는 실제 DB(H2) 기준
- 테스트 순서: 테이블 자동 생성 -> `@BeforeEach` 데이터 생성 -> 테스트 실행 -> `@AfterEach` 데이터 삭제

## 실행 명령
- `./gradlew test --tests "com.sfood.mb.app.memo.*"`
- `./gradlew test`

## 기대 결과
- Memo 관련 테스트 전부 통과
- 전체 테스트 회귀 통과

## 실행 결과
- 실행 일시: 2026-02-25
- 1차 실행: `./gradlew test --tests "com.sfood.mb.app.memo.*"` 실패
- 실패 원인: Spring Data 파생 쿼리 메서드에서 `zIndex` 정렬 속성 파싱 실패
- 조치: `MemoRepository`를 파생 메서드에서 명시적 `@Query`로 변경
- 2차 실행: `./gradlew test --tests "com.sfood.mb.app.memo.*"` 통과 (`BUILD SUCCESSFUL`)
- 회귀 실행: `./gradlew test` 통과 (`BUILD SUCCESSFUL`)
