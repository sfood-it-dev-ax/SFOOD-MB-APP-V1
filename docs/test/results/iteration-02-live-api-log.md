## Live API Test 2026-02-27 18:01:41 KST
- userId: live-user-20260227180141
- boardId: live-board-20260227180141
- memoId: live-memo-20260227180141
- PASS: GET /api/auth/me before login (status 401)
- PASS: POST /api/auth/login (status 200)
- PASS: login success payload
- PASS: GET /api/auth/me after login (status 200)
- PASS: me includes userId
- PASS: GET /api/memo-types (status 200)
- PASS: memo-types includes basic-yellow
- PASS: POST /api/boards (status 200)
- PASS: create board returns boardId
- PASS: GET /api/boards (status 200)
- PASS: board list contains new board
- PASS: PATCH /api/boards/{boardId}/name (status 200)
- PASS: board renamed
- PASS: POST /api/memos (status 200)
- PASS: create memo returns memoId
- PASS: GET /api/memos (status 200)
- PASS: memo list contains memo
- PASS: PATCH /api/memos/{memoId} (status 200)
- PASS: memo content updated
- PASS: DELETE /api/memos/{memoId} (status 200)
- PASS: DELETE /api/boards/{boardId} (status 200)
- PASS: POST /api/auth/logout (status 200)
- PASS: GET /api/auth/me after logout (status 401)

## Result
- PASS: live API end-to-end scenario completed on PostgreSQL
