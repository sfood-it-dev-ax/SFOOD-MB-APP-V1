# Iteration 08 Result

## Delivered
- Implemented board move API endpoint: `PATCH /api/boards/{boardId}/move`
- Added board hierarchy metadata fields (`parentBoardId`, `sortOrder`).
- Added move validation rules:
  - board cannot be its own parent
  - board move cannot create cycle in hierarchy
- Updated integration flow test to verify move success.

## Impact
- Frontend board move action now has backend endpoint support.
