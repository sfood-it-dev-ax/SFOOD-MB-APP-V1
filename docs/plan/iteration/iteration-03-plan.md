# Iteration 03 Plan

## Goal
Connect to real PostgreSQL(Supabase) and run full live API end-to-end test against running application.

## Scope
- Start backend with `postgres` profile and real DB connection.
- Resolve runtime DB connection issues if found.
- Execute full API flow test via HTTP calls (auth/board/memo/memo-type).
- Save detailed execution log and iteration result/change documents.

## Steps
1. Verify DB connection parameters and start app with postgres profile.
2. If boot fails, identify and fix connection/config issue.
3. Run live API scenario with unique IDs and session cookie handling.
4. Record pass/fail, commands, and fix history in docs.
