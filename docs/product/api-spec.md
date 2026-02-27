# API Spec (Frontend)

## 1. Common
- Base URL: `/api/v1`
- Auth: session cookie (`SESSION`)
- Content-Type: `application/json`
- Success envelope:
```json
{
  "success": true,
  "data": {},
  "message": "string",
  "timestamp": "2026-02-26T17:40:46.216282326"
}
```
- Error envelope:
```json
{
  "success": false,
  "errorCode": "UNAUTHORIZED",
  "message": "string",
  "timestamp": "2026-02-26T17:40:47.437922999"
}
```

## 2. Session/Cookie Handling
- Browser client must send credentials on every request after login.
- Frontend axios default:
```ts
import axios from "axios";

export const api = axios.create({
  baseURL: "/api/v1",
  withCredentials: true
});
```
- Frontend fetch example:
```ts
await fetch("/api/v1/auth/me", {
  method: "GET",
  credentials: "include"
});
```

## 3. Error Code Guide
- `UNAUTHORIZED`: no valid session or expired session (`401`)
- `VALIDATION_ERROR`: request field validation fail (`400`)
- `NOT_FOUND`: target board/memo not found (`404`)
- `CONFLICT`: duplicate or data integrity conflict (`409`)

Frontend handling recommendation:
- `401` -> redirect/login modal
- `400` -> show field/message from response
- `404` -> refresh list and show missing resource toast
- `409` -> show conflict toast and refresh related list

## 4. Auth API
1. `POST /auth/login`
- Request:
```json
{"googleToken":"devuser@example.com|Dev User|"}
```
- Response data:
```json
{"userId":"devuser@example.com","name":"Dev User","profileImage":""}
```
- Verification rule:
  - First successful login stores `googleToken` for the user.
  - Later login for same user must send the same `googleToken`.
  - If different, response is `401 UNAUTHORIZED` with `Google token mismatch`.

2. `GET /auth/me`
- Session user lookup

3. `POST /auth/logout`
- Invalidate current session

## 5. Board API
1. `GET /boards`
- Description: current user board list

2. `POST /boards`
- Request:
```json
{
  "boardId":"devuser@example.com_1772095242_b",
  "parentBoardId":null,
  "boardName":"TestBoard2",
  "sortOrder":101
}
```
- Validation:
  - `boardId`: non-blank
  - `boardName`: 1..20
  - `sortOrder`: integer

3. `PATCH /boards/{boardId}/name`
- Request:
```json
{"boardName":"Updated Board"}
```

4. `PATCH /boards/{boardId}/move`
- Request:
```json
{"parentBoardId":null,"sortOrder":2}
```

5. `DELETE /boards/{boardId}`
- Behavior: logical delete (`is_hide=true`)

## 6. Memo API
1. `GET /boards/{boardId}/memos`
- Description: memo list by board

2. `POST /boards/{boardId}/memos`
- Request:
```json
{
  "memoId":"devuser@example.com_1772095242_b_m1",
  "typeId":"TYPE_BASIC",
  "content":"third supabase test",
  "posX":210,
  "posY":145,
  "width":320,
  "height":200,
  "zIndex":2
}
```
- Required:
  - `memoId`, `typeId`, `posX`, `posY`, `width`, `height`, `zIndex`

3. `PATCH /memos/{memoId}/content`
- Request:
```json
{"content":"updated"}
```

4. `PATCH /memos/{memoId}/position`
- Request:
```json
{"posX":300,"posY":220}
```

5. `PATCH /memos/{memoId}/size`
- Request:
```json
{"width":360,"height":240}
```

6. `PATCH /boards/{boardId}/memos/zindex`
- Request:
```json
{"memos":[{"memoId":"devuser@example.com_1772095242_b_m1","zIndex":5}]}
```

7. `DELETE /memos/{memoId}`
- Behavior: logical delete (`is_hide=true`)

## 7. Memo Type API
1. `GET /memo-types`
- Description: active memo type list

## 8. Real Verification Snapshot (Iteration 09/10)
- `POST /auth/login` -> `200`
- `POST /boards` -> `200`
- `POST /boards/{boardId}/memos` -> `200`
- `GET /boards/{boardId}/memos` -> `200`
- Sample created IDs:
  - `boardId=devuser@example.com_1772095242_b`
  - `memoId=devuser@example.com_1772095242_b_m1`

## 9. Backend API-In Logs (Front Integration)
- Backend logs API reception at controller entry with `INFO` level.
- Log pattern:
  - `API IN - <METHOD> <PATH> (..., sessionId=...)`
- Covered controllers:
  - Auth: `/api/v1/auth/*`
  - Board: `/api/v1/boards/*`
  - Memo: `/api/v1/boards/{boardId}/memos`, `/api/v1/memos/*`
  - MemoType: `/api/v1/memo-types`
- If frontend reports "request sent but no backend log":
  1. Verify frontend base URL is `/api/v1`.
  2. Verify credential mode is enabled (`withCredentials: true` or `credentials: "include"`).
  3. Check browser Network tab for actual request URL/method and CORS/proxy behavior.

## 10. Known Failure History (Resolved)
- Symptom:
  - Multiple endpoints returned `500` and body message `Unexpected error occurred`.
- Root cause:
  - PostgreSQL schema mismatch for Spring Session table (`SPRING_SESSION`).
- Resolution:
  - `spring.session.jdbc.table-name=memo_board.SPRING_SESSION`
  - Postgres profile schema/search_path alignment to `memo_board`.

## 11. Troubleshooting Addendum (Front Test 01)
- Added backend-safe mapping for common non-business exceptions:
  - Malformed JSON/request body -> `400 VALIDATION_ERROR`
  - DB integrity violations (FK/unique conflicts) -> `409 CONFLICT`
- Backend now logs:
  - request-in: `API REQ - ...`
  - response-out: `API RES - ...`
  - exception cause: `GlobalExceptionHandler` warnings/errors with request URI
- Note on login replay:
  - Same user must send the same persisted `googleToken`.
  - Different token value for existing user returns `401 UNAUTHORIZED (Google token mismatch)`.
