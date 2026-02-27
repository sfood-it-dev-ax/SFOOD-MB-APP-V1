# Iteration 09 Test Result

## Test Date
- 2026-02-26 (KST)

## Environment
- Profile: `local`
- DB: Supabase PostgreSQL (`postgres` DB, `memo_board` schema)
- Command mode: debug run (`--spring.profiles.active=local --debug`)

## Executed Commands
1. App debug run
- `./gradlew bootRun --args='--spring.profiles.active=local --debug'`
- Result: PASS
- Key log:
  - `Started SfoodMbAppV1Application`
  - `Schema "memo_board" is up to date. No migration necessary.`

2. API flow (session login + board/memo create/list)
- `curl` sequence:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/boards`
  - `POST /api/v1/boards/{boardId}/memos`
  - `GET /api/v1/boards/{boardId}/memos`
- Result: PASS
- Test identifiers:
  - `BOARD_ID=devuser@example.com_1772095242_b`
  - `MEMO_ID=devuser@example.com_1772095242_b_m1`
- HTTP:
  - login: `200`
  - create board: `200`
  - create memo: `200`
  - list memos: `200`

3. Automated tests
- `./gradlew test`
- Result: PASS
- Note:
  - Initial parallel run with `build` caused test report file collision.
  - Re-run `test` standalone passed.

4. Build verification
- `./gradlew build`
- Result: PASS

## API Evidence (Summary)
- `POST /api/v1/boards` response included:
  - `boardId=devuser@example.com_1772095242_b`
  - `boardName=TestBoard2`
- `POST /api/v1/boards/{boardId}/memos` response included:
  - `memoId=devuser@example.com_1772095242_b_m1`
  - `typeId=TYPE_BASIC`
  - `posX=210.0`, `posY=145.0`
- `GET /api/v1/boards/{boardId}/memos` returned created memo row.
