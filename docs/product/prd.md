# PRD - Post-it Board Backend (Iteration 01)

## Objective
Iteration 01 delivers core backend APIs required for basic memo board usage in local development.

## In Scope
- Session-based auth APIs: login, logout, me.
- Board APIs: create, list, rename, logical delete.
- Memo APIs: create, list by board, update content/position/size/z-index, logical delete.
- Memo type list API.
- Consistent API response and error contract.

## Out of Scope
- Google OAuth integration.
- Persistent database integration.
- Board tree drag/move API.
- Rich text formatting persistence detail.

## Success Criteria
- API contracts satisfy `docs/guide/testing-strategy.md` minimum coverage for Iteration 01.
- Service layer keeps business logic, controller remains thin.
- All delete operations are logical delete (`isHide=true`).
- `./gradlew test` and `./gradlew build` pass.
