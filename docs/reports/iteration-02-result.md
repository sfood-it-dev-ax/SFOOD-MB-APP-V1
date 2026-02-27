# Iteration 02 Result

## Delivered Scope
- Added JPA and H2 runtime configuration for local persistence validation.
- Converted `User`, `Board`, `Memo`, `MemoType` to JPA entities.
- Added Spring Data repositories and JPA adapter implementations for existing repository interfaces.
- Scoped in-memory repositories to `memory` profile to keep interface-first compatibility.
- Added memo type bootstrap initializer for default master data.

## Key Decisions
- Preserved controller/service interface contracts from iteration 01.
- Kept session auth flow unchanged while moving persistence implementation.
- Used `mb_*` table naming to avoid reserved-word conflicts in H2 local environment.

## Notable Fix During Validation
- Resolved Spring Data derived-query failure for memo z-index sort by aligning entity field/query method naming.

## Remaining Gaps
- Real PostgreSQL/Supabase schema alignment pending.
- Real Google token verification pending.
- Full Spring Security filter-chain hardening pending.
