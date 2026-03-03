# Iteration 04 Plan

## Goal
Create schema via Spring/Hibernate option, verify table creation, and rerun full API live test.

## Scope
- Add schema auto-create option in postgres profile.
- Boot app with a new schema name.
- Verify schema/table creation with direct DB metadata query.
- Run live API E2E test again.
- Document all outcomes and fixes.

## Steps
1. Update postgres profile with namespace creation option.
2. Run app with new `DB_SCHEMA` value.
3. Execute full API scenario using unique IDs.
4. Query `information_schema` to confirm schema/tables.
5. Save results to iteration docs.
