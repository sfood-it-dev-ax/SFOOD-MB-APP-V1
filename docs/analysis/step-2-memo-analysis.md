# Step 2 분석: Memo API 1차 구현

기준 문서:
- `docs/guide/requirement.md` (MEMO-001 ~ MEMO-021 중심)
- `docs/guide/db-spec.md`

## 1. 이번 단계 구현 범위
- boardId 기준 메모 목록 조회
- 메모 생성
- 메모 수정(내용/위치/크기/z-index/스타일)
- 메모 삭제(HIDE=true 논리 삭제)

## 2. 제외 범위(다음 단계)
- 인증/세션 연계 권한 검증
- 컨텍스트 메뉴 기반 복수 메모 노출순서 일괄 재계산 API
- 에디터 상세 기능(체크박스/리스트 변환 로직)

## 3. API 설계
- `GET /api/v1/memos?boardId={boardId}`
  - 보드의 메모 목록 조회(HIDE=false)
- `POST /api/v1/memos`
  - 메모 생성
- `PATCH /api/v1/memos/{memoId}`
  - 메모 부분 수정(내용/좌표/크기/z-index/스타일)
- `DELETE /api/v1/memos/{memoId}`
  - 메모 삭제(HIDE=true)

## 4. 데이터 모델
- `Memo`
  - `memoId` (PK, String)
  - `boardId` (String)
  - `memoTypeId` (String)
  - `content` (String)
  - `posX`, `posY`, `width`, `height`, `zIndex` (int)
  - `textColor`, `fontFamily`, `fontSize` (style)
  - `hide` (논리 삭제)
  - `createdAt`, `updatedAt`

## 5. 규칙
- 조회/수정/삭제는 `hide=false` 기준
- 메모 생성 시 대상 보드가 존재하고 `hide=false`여야 함
- 삭제는 물리 삭제 대신 `hide=true`

## 6. 검증 전략
- 서비스 테스트: CRUD + 논리 삭제 + 보드 검증
- 컨트롤러 테스트: 실제 DB(H2) 기반으로 데이터 생성/삭제 순서 검증

## 7. 리스크 및 대응
- 현재 인증 미구현으로 사용자 단위 권한 검증은 Step 3에서 보강
- z-index 전체 재정렬은 2차 확장 대상으로 남겨 둠
