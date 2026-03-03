## Live API Test With New Schema 2026-02-27 18:08:27 KST
- userId: schema-live-user-20260227180827
- boardId: schema-live-board-20260227180827
- memoId: schema-live-memo-20260227180827
- schema: memo_board_v4_20260227180730
- PASS: GET /api/auth/me before login (401)
- PASS: POST /api/auth/login (200)
- PASS: login payload
- PASS: GET /api/memo-types (200)
- PASS: POST /api/boards (200)
- PASS: POST /api/memos (200)
- PASS: PATCH /api/memos/{memoId} (200)
- PASS: DELETE /api/memos/{memoId} (200)
- PASS: DELETE /api/boards/{boardId} (200)
- PASS: POST /api/auth/logout (200)

## Result
- PASS: API E2E passed on newly targeted schema
