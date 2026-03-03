## Live API Test (memo_board_v3) 2026-02-27 18:13:56 KST
- userId: v3-live-user-20260227181356
- boardId: v3-live-board-20260227181356
- memoId: v3-live-memo-20260227181356
- schema: memo_board_v3
- PASS: GET /api/auth/me before login (401)
- PASS: POST /api/auth/login (200)
- PASS: login payload
- PASS: GET /api/memo-types (200)
- PASS: memo-types has basic-yellow
- PASS: POST /api/boards (200)
- PASS: POST /api/memos (200)
- PASS: PATCH /api/memos/{memoId} (200)
- PASS: DELETE /api/memos/{memoId} (200)
- PASS: DELETE /api/boards/{boardId} (200)
- PASS: POST /api/auth/logout (200)

## Result
- PASS: memo_board_v3 only live API test completed
