# Iteration 02 Result

## Delivered
- Repository layer migrated to JPA adapters.
- Domain models migrated to JPA entities and mapped tables.
- Added PostgreSQL runtime dependency and `application-postgres.properties` profile.
- Added H2 default datasource for local/test execution.
- Removed in-memory repository implementations.
- Updated tests to remove in-memory direct dependency.

## Technical Decisions
- Preserved service interfaces and controller contracts to avoid API regression.
- Kept memo types as repository-managed default static catalog for stable behavior.
- Added explicit transactional boundaries on service methods.

## Remaining Gap
- Real PostgreSQL runtime has not been executed in this iteration (profile/config prepared, not connected in CI/local command run).
