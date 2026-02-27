# Iteration 06 Plan

## Goal
Align runtime configuration for production-like PostgreSQL usage while preserving local H2 test stability.

## Step-by-step Plan
1. Analysis document
- Define profile strategy (`local`, `postgres`) and expected behavior.
- Output: analysis with config matrix and risks.

2. Profile configuration
- Split common vs profile-specific properties.
- Keep local/test profile on H2.
- Add postgres profile with datasource/session compatibility keys.

3. Compatibility tuning
- Ensure JPA and Spring Session JDBC settings work across profiles.
- Add minimal fail-fast notes for missing postgres env values.

4. Validation and docs
- Run `./gradlew test` and `./gradlew build` using default local profile.
- Record outputs in iteration-06 test/result documents.

5. Approval gate
- Request user approval before Iteration 07.

## Phase Policy
- Out-of-plan change requests are documented first.
