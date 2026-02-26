# Backend Development Plan (Java Spring)

기준 문서:
- `docs/guide/requirement.md`
- `docs/guide/db-spec.md`

개발 원칙:
- 단계별로 `분석 문서 -> 구현 -> 테스트 문서 -> 테스트 실행 -> 사용자 승인` 순서로 진행
- 각 단계 완료 후 승인 없이는 다음 단계로 진행하지 않음
- 논리 삭제(HIDE), 계층 구조(보드), 메모 편집/배치 저장 규칙 우선 반영

## Step 1. 프로젝트 기반 및 보드 API 1차 구현
목표:
- Spring Boot API 기본 구조 수립
- Board 도메인 CRUD(조회/생성/수정/삭제(HIDE)) 구현

범위:
- Board 엔티티/리포지토리/서비스/컨트롤러
- 요청/응답 DTO 및 유효성 검증
- 공통 예외/에러 응답
- 테스트(서비스/컨트롤러)

산출물:
- `docs/analysis/step-1-board-analysis.md`
- API 코드
- `docs/test/step-1-board-test.md`

승인 게이트:
- 테스트 통과 후 사용자 승인

## Step 2. 메모 API 1차 구현
목표:
- Memo 도메인 CRUD 및 위치/크기/z-index 업데이트 구현

범위:
- Memo 엔티티/리포지토리/서비스/컨트롤러
- boardId 기준 목록 조회
- 논리 삭제(HIDE)

산출물:
- `docs/analysis/step-2-memo-analysis.md`
- API 코드
- `docs/test/step-2-memo-test.md`

승인 게이트:
- 테스트 통과 후 사용자 승인

## Step 3. 인증/세션 구조 1차 구현
목표:
- 세션 기반 인증 흐름 골격 구성
- 로그인/로그아웃/세션확인 API 제공

범위:
- 인증 서비스/컨트롤러
- 세션 체크 인터셉터(또는 필터)
- 테스트(인증 흐름)

산출물:
- `docs/analysis/step-3-auth-analysis.md`
- API 코드
- `docs/test/step-3-auth-test.md`

승인 게이트:
- 테스트 통과 후 사용자 승인

## Step 4. 통합 정리 및 운영 설정
목표:
- DB 연동 프로파일 정리
- 운영 기준 문서/환경값 정리

범위:
- `application.properties`/프로파일 분리
- MySQL 연결 가이드
- 전체 스모크 테스트 문서

산출물:
- `docs/analysis/step-4-integration-analysis.md`
- 설정 코드
- `docs/test/step-4-integration-test.md`

승인 게이트:
- 테스트 통과 후 사용자 승인 후 종료
