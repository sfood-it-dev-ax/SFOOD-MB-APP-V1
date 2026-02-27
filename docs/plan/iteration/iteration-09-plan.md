# Iteration 09 Plan

## Goal
Run the backend in `local` debug mode with Supabase PostgreSQL, apply schema migrations, and validate data creation through API calls.

## Scope
- Confirm `local` Spring profile DB settings match architecture document.
- Start application in debug mode with `local` profile.
- Verify Flyway migrations create required tables.
- Execute authenticated API flow to create board/memo data.
- Record test execution and outcomes.

## Step-by-step Plan
1. Analysis document
- Capture target behavior and test scenario.
- Output: `docs/reports/iteration-09-analysis.md`.

2. Environment/config validation
- Validate `application-local.properties` points to Supabase PostgreSQL.
- Ensure session timeout and Flyway settings are aligned.

3. Runtime + migration validation
- Run `bootRun` with `--spring.profiles.active=local --debug`.
- Check startup logs for successful Flyway migration.

4. API data creation test
- Login to obtain session.
- Create board and memo, then read back list endpoints.

5. Documentation + approval gate
- Write test result document and iteration result report.
- Wait for user approval before next iteration.

## Out of Scope
- New business feature development.
- Authorization role changes.
