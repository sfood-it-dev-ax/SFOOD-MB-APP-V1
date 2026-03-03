# Iteration 01 Plan

## 메타 정보
- Iteration: 01
- 작성일: 2026-03-03
- 상태: Active
- 연계 문서: `docs/product/prd.md`, `docs/product/data-spec.md`

## 목표
- 인증/보드/메모 핵심 기능 기준선을 안정화하고, 테스트 전략과 문서 체계를 동시에 정착한다.

## 범위
- 포함
- 인증 세션 플로우(login/logout/me)
- 보드 CRUD 및 논리 삭제
- 메모 CRUD 및 위치/크기/z-index 변경
- API 에러 계약(400/401/404/409)
- 제외
- 검색/정렬/파일첨부/Import-Export
- JWT 및 ID/PW 로그인

## 작업 계획
- [x] P1. 지침/가이드 검토 및 누락 문서 식별
- [x] P2. PRD/Data Spec 초안 작성
- [x] P3. 인증 API 구현 점검 및 테스트 보강
- [x] P4. 보드 API 구현 점검 및 테스트 보강
- [x] P5. 메모 API 구현 점검 및 테스트 보강
- [x] P6. 통합 검증(`./gradlew test`, 필요 시 `./gradlew build`)
- [x] P7. test-result/result 문서 최종화

## 테스트 계획
- 단위 테스트: 서비스 비즈니스 로직
- Web MVC 테스트: 컨트롤러 계약/상태코드
- 통합 스모크: 컨텍스트 로드 및 핵심 플로우
- 최소 커버리지 대상
- auth login/logout/me
- board create/list/update/delete
- memo create/list/update/delete + position/size/z-index
- memo type list

## 완료 기준(DoD)
- 계획/분석/테스트결과/결과 문서가 모두 존재한다.
- 테스트 명령/결과가 문서로 기록된다.
- API 에러 계약과 응답 형식이 일관된다.
- 무음 실패 경로가 없다.

## 리스크
- 세션/프로파일 차이로 IDE와 CLI 결과가 다를 수 있음
- 자동 저장/순서 재정렬 로직의 경계 조건 누락 가능성
- 기존 코드 구조와 인터페이스 우선 규칙 간 불일치 가능성
