# API Spec & Test Result (Iteration 01)

## 문서 정보
- 작성일: 2026-03-03
- 기준: `docs/product/prd.md`
- 테스트 환경: Supabase PostgreSQL (`aws-1-ap-south-1.pooler.supabase.com:6543`, schema `memo_board_v4`)

## 공통
- Base URL: `/api/v1`
- 인증 방식: HttpSession (`POST /auth/login` 이후 세션 쿠키 사용)
- 응답 형식: `success`, `data`, `message`, `errorCode`, `timestamp`

## Auth API

### POST `/api/v1/auth/login`
- 요청
```json
{
  "googleAccount": "tester+1740971234567@sfood.com",
  "token": "dummy-token",
  "name": "Tester"
}
```
- 결과: `200 OK`
- DB 반영
- `users` 신규 row 생성 확인 (`user_id=googleAccount`)
- 신규 사용자일 때 `boards`에 기본 보드(`{account}_default`) 생성 확인

### GET `/api/v1/auth/me`
- 결과: `200 OK`
- 검증: 세션 사용자 정보 반환 확인

### POST `/api/v1/auth/logout`
- 결과: `200 OK`
- 검증: 이후 `GET /api/v1/auth/me` 호출 시 `401 UNAUTHORIZED`

## Board API

### GET `/api/v1/boards`
- 결과: `200 OK`
- 검증: 로그인 사용자 보드 목록 반환

### POST `/api/v1/boards`
- 요청
```json
{
  "boardId": "tester+1740971234567@sfood.com_ab12!cd34",
  "parentBoardId": null,
  "boardName": "업무보드",
  "sortOrder": 1
}
```
- 결과: `200 OK`
- DB 반영: `boards.board_id` row 생성 확인

### PATCH `/api/v1/boards/{boardId}/name`
- 요청
```json
{
  "boardName": "업무보드-수정"
}
```
- 결과: `200 OK`
- DB 반영: `board_name` 변경 확인

### PATCH `/api/v1/boards/{boardId}/move`
- 요청
```json
{
  "parentBoardId": null,
  "sortOrder": 2
}
```
- 결과: `200 OK`
- DB 반영: `sort_order` 변경 확인

### DELETE `/api/v1/boards/{boardId}`
- 결과: `200 OK`
- DB 반영: `is_hide=true` 확인

## Memo Type API

### GET `/api/v1/memo-types`
- 결과: `200 OK`
- 검증: 활성 메모 타입 목록 반환

## Memo API

### POST `/api/v1/boards/{boardId}/memos`
- 요청
```json
{
  "memoId": "tester+1740971234567@sfood.com_ab12!cd34_xy12#zz90",
  "typeId": "TYPE_BASIC",
  "content": "초기 메모",
  "posX": 100,
  "posY": 120,
  "width": 240,
  "height": 180,
  "zIndex": 1
}
```
- 결과: `200 OK`
- DB 반영: `memos` 신규 row 생성 확인

### GET `/api/v1/boards/{boardId}/memos`
- 결과: `200 OK`
- 검증: 보드별 메모 목록 반환

### PATCH `/api/v1/memos/{memoId}/content`
- 요청
```json
{ "content": "수정 메모" }
```
- 결과: `200 OK`
- DB 반영: `content` 변경 확인

### PATCH `/api/v1/memos/{memoId}/position`
- 요청
```json
{ "posX": 200, "posY": 240 }
```
- 결과: `200 OK`
- DB 반영: `pos_x`, `pos_y` 변경 확인

### PATCH `/api/v1/memos/{memoId}/size`
- 요청
```json
{ "width": 320, "height": 260 }
```
- 결과: `200 OK`
- DB 반영: `width`, `height` 변경 확인

### PATCH `/api/v1/boards/{boardId}/memos/zindex`
- 요청
```json
{
  "memos": [
    { "memoId": "tester+1740971234567@sfood.com_ab12!cd34_xy12#zz90", "zIndex": 3 }
  ]
}
```
- 결과: `200 OK`
- DB 반영: `z_index` 변경 확인

### DELETE `/api/v1/memos/{memoId}`
- 결과: `200 OK`
- DB 반영: `is_hide=true` 확인

## 실패/오류 분기 확인
- 무세션 접근 `GET /api/v1/auth/me` -> `401 UNAUTHORIZED` 확인
- Bean Validation 오류/미인증/리소스 없음/충돌 분기를 중앙 예외처리로 매핑

## Supabase 연결 특이사항
- PgBouncer 환경에서 prepared statement 충돌 회피를 위해 JDBC URL 옵션 사용
- `prepareThreshold=0`
- `preferQueryMode=simple`
