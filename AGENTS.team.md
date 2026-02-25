# AGENTS Team Custom

This file defines repository-specific working rules for `SFOOD-MB-APP-V1`.

## Scope

- Apply these rules to all code, docs, and config changes in this repository.
- If a rule here conflicts with `AGENTS.md`, this file wins for this repository.

## Delivery standard

- Ship complete, runnable outcomes for the requested scope.
- Keep changes minimal and focused; avoid unrelated refactors.
- Reuse existing patterns and naming before adding new abstractions.

## Required checks

- Run `./gradlew test` before finishing when Java code changes are made.
- Run `./gradlew build` when changes affect dependency configuration, Spring wiring, or application startup behavior.
- If tests are missing or blocked by environment limits, state that explicitly and run a minimal smoke check (for example `./gradlew classes`).
- Include commands run and pass/fail result in the final report.

## Git workflow

- Branch naming: `feature/<topic>`, `fix/<topic>`, `chore/<topic>`.
- Commit format: `<type>: <short description>` where type is one of `feat`, `fix`, `refactor`, `docs`, `test`, `chore`.
- Stage only files related to the current request.
- Do not amend existing commits unless explicitly requested.

## PR checklist

- Problem and approach are explained in 3-5 lines.
- User-visible behavior changes are listed clearly.
- Validation evidence is included (commands and key results).
- Risks and rollback plan are documented for non-trivial changes.

## Java/Spring implementation rules

- Respect package layering: `controller`, `service`, `repository`, `domain` roles must stay clear.
- Keep REST contracts stable; if request/response schemas change, document migration impact.
- Use explicit HTTP status codes and consistent error payloads from a centralized handler.
- Prefer Spring Boot starters and managed versions; avoid ad-hoc dependency version pinning unless justified.
- Add or update tests with changes; use `@WebMvcTest` for controllers, unit tests for services, and integration tests when query/transaction behavior changes.

## Safety

- Never run destructive git/file operations without explicit approval.
- Never revert unrelated user changes.
- If unexpected workspace changes appear during work, stop and ask.
