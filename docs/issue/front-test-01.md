# Iteration 05 Test Result (Live API)

## Command
- `npm run test:api`

## Result Summary
- PASS: Login, Me, GetBoards, CreateBoard, MemoTypes, Logout
- FAIL (500): RenameBoard, MoveBoard, CreateMemo, GetMemos, UpdateMemoContent, UpdateMemoPosition, UpdateMemoSize, UpdateMemoZIndex, DeleteMemo, DeleteBoard

## Raw Endpoint Result
- PASS [200] POST /auth/login
- PASS [200] GET /auth/me
- PASS [200] GET /boards
- PASS [200] POST /boards
- FAIL [500] PATCH /boards/{boardId}/name
- FAIL [500] PATCH /boards/{boardId}/move
- PASS [200] GET /memo-types
- FAIL [500] POST /boards/{boardId}/memos
- FAIL [500] GET /boards/{boardId}/memos
- FAIL [500] PATCH /memos/{memoId}/content
- FAIL [500] PATCH /memos/{memoId}/position
- FAIL [500] PATCH /memos/{memoId}/size
- FAIL [500] PATCH /boards/{boardId}/memos/zindex
- FAIL [500] DELETE /memos/{memoId}
- FAIL [500] DELETE /boards/{boardId}
- PASS [200] POST /auth/logout

## Notes
- Failure message body returned by backend: `Unexpected error occurred`.
- This requires backend log/stacktrace analysis for exact root cause.

## Follow-up
- Troubleshooting and remediation result:
  - `docs/issue/front-test-01-troubleshooting.md`
