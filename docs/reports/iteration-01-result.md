# Iteration 01 Result

## Delivered Scope
- Implemented common API response schema and global exception handler.
- Implemented session-based auth APIs (`login`, `logout`, `me`) with local mock token parsing.
- Implemented board APIs (`list`, `create`, `rename`, `move`, `logical delete`).
- Implemented memo APIs (`list`, `create`, `content/position/size update`, `z-index batch update`, `logical delete`).
- Implemented memo-type list API.
- Added end-to-end API flow integration test.

## Key Decisions
- Used interface-first service/repository design with in-memory adapter implementations for iteration speed.
- Enforced logical delete (`isHide`) for board/memo delete endpoints.
- Enforced session auth checks on protected APIs through auth service session resolution.

## Gaps / Next Iteration Candidates
- Replace mock token parsing with real Google token verification.
- Replace in-memory repositories with persistent DB (JPA/PostgreSQL).
- Add Spring Security + Spring Session JDBC hardening.
