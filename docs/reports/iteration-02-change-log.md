# Iteration 02 Change Log

## Build/Config
- Updated `build.gradle`
  - Added `spring-boot-starter-data-jpa`
  - Added runtime `h2`, `postgresql`
- Updated `application.properties` with default H2 datasource and JPA options.
- Added `application-postgres.properties` for PostgreSQL profile.

## Code
- Converted domain classes to JPA entities:
  - `User`, `Board`, `Memo`, `MemoType`
- Added Spring Data interfaces:
  - `SpringDataUserJpaRepository`, `SpringDataBoardJpaRepository`, `SpringDataMemoJpaRepository`, `SpringDataMemoTypeJpaRepository`
- Added JPA adapter repositories:
  - `JpaUserRepository`, `JpaBoardRepository`, `JpaMemoRepository`, `JpaMemoTypeRepository`
- Removed in-memory repository implementations.
- Added transactional boundaries to service implementations.

## Tests
- Updated unit tests to mock repository interfaces instead of in-memory implementations.
- Revalidated integration flow and build.
