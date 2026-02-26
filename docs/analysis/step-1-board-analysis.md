# Step 1 분석: Board API 1차 구현

기준 문서:
- `docs/guide/requirement.md` (BOARD-001 ~ BOARD-014 중심)
- `docs/guide/db-spec.md` (MySQL 8.0, 논리 삭제 요구)

## 1. 이번 단계 구현 범위
- 보드 목록 조회
- 보드 생성(최상위/하위)
- 보드명 수정
- 보드 삭제(HIDE=true 논리 삭제)

## 2. 제외 범위(다음 단계)
- 보드 드래그 이동(BOARD-015)
- 인증/세션/Google OAuth
- 메모 API 전체

## 3. API 설계(초안)
- `GET /api/v1/boards?userEmail={email}`
  - 사용자 보드 목록 조회(HIDE=false)
- `POST /api/v1/boards`
  - 보드 생성
- `PATCH /api/v1/boards/{boardId}/name`
  - 보드명 수정
- `DELETE /api/v1/boards/{boardId}`
  - 보드 삭제(HIDE=true)

## 4. 데이터 모델
- `Board`
  - `boardId` (PK, String)
  - `userEmail` (소유 사용자)
  - `name` (최대 20자)
  - `parentBoardId` (nullable)
  - `sortOrder` (기본 0)
  - `hide` (논리 삭제 플래그)
  - `createdAt`, `updatedAt`

## 5. 기술 결정
- Spring Web + Spring Data JPA + Validation
- H2 기반 로컬/테스트 기본 실행
- MySQL 드라이버는 운영 확장 대비 runtime dependency로 추가
- 공통 예외 응답 포맷 제공

## 6. 검증 전략
- 서비스 테스트: CRUD + 논리 삭제 동작 검증
- 컨트롤러 테스트: 상태코드/입력검증/JSON 응답 검증

## 7. 리스크 및 대응
- 현재 인증 미구현 상태라 사용자 식별은 `userEmail` 파라미터/필드로 처리
- 인증 단계 구현 시 사용자 식별 소스를 세션 기반으로 대체 예정
