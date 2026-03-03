# Iteration 09 Analysis

## Symptom
- `PATCH /api/boards/{boardId}/move` returned `500 INTERNAL_ERROR` while other board APIs (create/rename/delete/list) succeeded.

## Root Cause
- Board move introduced hierarchy fields (`parentBoardId`, `sortOrder`) and writes them during move.
- Runtime PostgreSQL schema could be out-of-sync with recent board hierarchy changes (missing `parent_board_id` / `sort_order`), which causes SQL-level failure only on move update path.

## Why It Appeared as 500
- SQL/ORM runtime exceptions in move path are handled by global unhandled exception branch and returned as `INTERNAL_ERROR`.

## Fix Strategy
- Stabilize ORM-to-DB mapping with explicit board column names.
- Add PostgreSQL startup schema compatibility initializer that ensures required board hierarchy columns exist before move requests are processed.
