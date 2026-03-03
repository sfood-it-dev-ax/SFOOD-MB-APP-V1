# Iteration 02 Plan

## Goal
Migrate repository layer from in-memory storage to JPA-based persistence with PostgreSQL-ready configuration.

## Scope
- Add Spring Data JPA and DB runtime dependencies.
- Convert domain models to JPA entities.
- Replace in-memory repository implementations with JPA adapters.
- Keep existing service/controller contracts unchanged.
- Add default and postgres profile datasource configuration.
- Keep auth/board/memo API behavior unchanged.

## Steps
1. Add JPA/H2/PostgreSQL dependencies and datasource properties.
2. Convert `User/Board/Memo/MemoType` to JPA entities.
3. Implement Spring Data repositories and adapter implementations.
4. Remove in-memory repository implementations and update unit tests.
5. Execute `./gradlew test` and `./gradlew build`.
