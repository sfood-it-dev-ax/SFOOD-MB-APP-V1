# Data Spec - Post-it Board

## 문서 정보
- 버전: v1.0
- 작성일: 2026-03-03
- 참조: `docs/guide/development-rules.md`, `docs/guide/requirements.md`

## 공통 규칙
- 삭제는 물리 삭제가 아닌 논리 삭제(`isHide=true`)를 사용한다.
- 모든 API 요청/응답은 DTO 경계를 사용한다.
- 인증은 세션 기반이며, 미인증 접근은 `401`을 반환한다.
- 에러 응답 스키마는 중앙 예외 처리 정책을 따른다.

## 도메인 엔터티

### User
- `id`: Long, PK
- `email`: String, unique, not null
- `displayName`: String, nullable
- `createdAt`: LocalDateTime
- `updatedAt`: LocalDateTime

### Board
- `id`: String, PK
- `userId`: Long, not null
- `name`: String, not null, max 20
- `parentBoardId`: String, nullable
- `sortOrder`: Integer, not null
- `isHide`: Boolean, not null, default false
- `createdAt`: LocalDateTime
- `updatedAt`: LocalDateTime

### Memo
- `id`: String, PK
- `boardId`: String, not null
- `memoTypeId`: String, not null
- `contentHtml`: String, nullable
- `x`: Integer, not null
- `y`: Integer, not null
- `width`: Integer, not null
- `height`: Integer, not null
- `zIndex`: Integer, not null
- `isHide`: Boolean, not null, default false
- `createdAt`: LocalDateTime
- `updatedAt`: LocalDateTime

### MemoType
- `id`: String, PK
- `name`: String, not null
- `defaultColor`: String, not null
- `shape`: String, not null
- `isEnabled`: Boolean, not null

## ID 규칙
- Board ID: `{account}_{random10}`
- Memo ID: `{boardId}_{random10}`
- `random10`은 영문/숫자/특수문자 조합 10자리

## API DTO 계약

### Auth
- `POST /api/auth/login`
  - Request: OAuth 인가 결과 토큰(또는 인가 코드)
  - Response: 로그인 사용자 정보 + 세션 생성 결과
- `POST /api/auth/logout`
  - Response: 성공 여부
- `GET /api/auth/me`
  - Response: 세션 사용자 정보

### Board
- `POST /api/boards`
  - Request: `boardId`, `name`, `parentBoardId`, `sortOrder`
  - Validation: `name` max 20, not blank
- `GET /api/boards`
  - Response: 사용자 보드 트리 목록(`isHide=false`)
- `PATCH /api/boards/{boardId}`
  - Request: `name` 또는 위치/부모 변경 필드
- `DELETE /api/boards/{boardId}`
  - 동작: `isHide=true` 업데이트

### Memo
- `POST /api/memos`
  - Request: `memoId`, `boardId`, `memoTypeId`, `contentHtml`, `x`, `y`, `width`, `height`, `zIndex`
- `GET /api/memos?boardId={boardId}`
  - Response: 보드별 메모 목록(`isHide=false`)
- `PATCH /api/memos/{memoId}`
  - Request: 내용/위치/크기/z-index 변경 필드
- `DELETE /api/memos/{memoId}`
  - 동작: `isHide=true` 업데이트

## Validation 규칙
- Board 이름은 20자를 초과할 수 없다.
- PathVariable/RequestParam은 명시적 이름을 사용한다.
  - `@PathVariable("boardId")`
  - `@RequestParam("sortOrder")`
- Memo 좌표/크기/z-index는 null 불가이며 정수형을 유지한다.

## 에러 계약
- `400 VALIDATION_ERROR`: 잘못된 요청 바디/파라미터
- `401 UNAUTHORIZED`: 미인증/만료 세션
- `404 NOT_FOUND`: 대상 리소스 없음
- `409 CONFLICT`: 데이터 무결성 충돌

## 트랜잭션/정합성
- 서비스 레이어에서 트랜잭션 경계를 명시한다.
- 메모 z-index 변경은 영향 범위 메모를 함께 갱신해 정렬 정합성을 유지한다.
- 자동 저장 실패 시 클라이언트는 오류를 인지할 수 있어야 하며, 서버는 실패 원인을 일관된 에러 스키마로 반환한다.
