# Iteration 05 Plan

## Goal
Use `memo_board_v3` only, guarantee default memo type data in DB, and re-run live API tests.

## Scope
- Add DB-seeding initializer for `memo_types`.
- Ensure postgres pooler compatibility for runtime startup.
- Run DB data check and full API live test on `memo_board_v3` only.
- Document all commands/results.

## Steps
1. Implement `memo_types` startup seed when empty.
2. Adjust postgres URL defaults for pooler stability.
3. Boot app with `DB_SCHEMA=memo_board_v3` only.
4. Verify `memo_types` row count and values.
5. Run full live API E2E and save logs.
