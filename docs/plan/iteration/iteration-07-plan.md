# Iteration 07 Plan

## Goal
Provide baseline PostgreSQL schema automation using Flyway for core domain tables and session tables.

## Step-by-step Plan
1. Analysis document
- Define migration scope and ordering.
- Output: analysis with migration strategy and risk notes.

2. Flyway setup
- Add Flyway dependency.
- Configure postgres profile to run Flyway migrations.
- Keep local profile Flyway disabled.

3. Migration scripts
- Add core table DDL migration (users, boards, memo_types, memos).
- Add session table DDL migration for Spring Session JDBC.
- Seed baseline memo type data.

4. Validation
- Run `./gradlew test` and `./gradlew build` in local profile.
- Record outcomes in iteration-07 test document.

5. Approval gate
- Request user approval before Iteration 08.

## Phase Policy
- Any scope changes are documented first.
