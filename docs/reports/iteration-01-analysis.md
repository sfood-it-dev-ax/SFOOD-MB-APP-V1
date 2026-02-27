# Iteration 01 Analysis

## Source Documents Reviewed
- `docs/guide/requirements.md`
- `docs/guide/architectures.md`
- `AGENTS.md`
- `AGENTS.team.md`

## Implementation Scope (Phase A)
- Common API envelope and centralized error response.
- Session auth APIs:
  - `POST /api/v1/auth/login`
  - `POST /api/v1/auth/logout`
  - `GET /api/v1/auth/me`
- Board APIs:
  - `GET /api/v1/boards`
  - `POST /api/v1/boards`
  - `PATCH /api/v1/boards/{boardId}/name`
  - `PATCH /api/v1/boards/{boardId}/move`
  - `DELETE /api/v1/boards/{boardId}`
- Memo APIs:
  - `GET /api/v1/boards/{boardId}/memos`
  - `POST /api/v1/boards/{boardId}/memos`
  - `PATCH /api/v1/memos/{memoId}/content`
  - `PATCH /api/v1/memos/{memoId}/position`
  - `PATCH /api/v1/memos/{memoId}/size`
  - `PATCH /api/v1/boards/{boardId}/memos/zindex`
  - `DELETE /api/v1/memos/{memoId}`
- Memo type API:
  - `GET /api/v1/memo-types`

## Design Decisions
- Interface-first: service/repository interfaces + concrete in-memory adapters.
- Session auth: HttpSession based; JWT not used.
- Logical delete: board/memo delete updates `isHide=true`.
- Data ownership checks: board/memo access limited to logged-in user.

## Assumptions
- Real Google token verification is deferred; local mock parsing is used in iteration 01.
- Persistence is in-memory for fast local validation; persistent DB integration is planned for next iteration.

## Validation Rules
- `boardName` max 20 chars.
- Required IDs are non-blank.
- Update APIs validate required numeric/text fields.

## Risks and Follow-ups
- In-memory storage resets on restart.
- No real OAuth token introspection in this iteration.
- Spring Security/JDBC session hardening should be added in next iteration.
