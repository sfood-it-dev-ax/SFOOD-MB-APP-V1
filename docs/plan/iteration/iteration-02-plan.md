# Iteration 02 Plan

## Goal
Migrate storage layer from in-memory to JPA-based persistence while preserving API contracts delivered in Iteration 01.

## Step-by-step Plan
1. Analysis document
- Define migration target, schema mapping, and compatibility constraints.
- Output: analysis with entity/repository mapping and risk notes.

2. Persistence migration
- Introduce JPA entities for users/boards/memos/memo_types.
- Add Spring Data JPA repository interfaces.
- Replace in-memory adapters in service implementations with JPA repositories.
- Keep logical delete semantics.

3. Configuration and bootstrap
- Configure datasource defaults for local development and test profile.
- Seed default memo types for API compatibility.

4. Test updates and execution
- Update integration test stability for persistent repository behavior.
- Run `./gradlew test` and `./gradlew build`.
- Record outputs in test result document.

5. Result report and approval gate
- Summarize migration outcomes, remaining gaps, and risks.
- Request user approval before Iteration 03.

## Phase Policy
- Any scope addition or user-requested change is documented first in analysis/result/test docs.
