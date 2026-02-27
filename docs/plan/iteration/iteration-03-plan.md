# Iteration 03 Plan

## Goal
Harden session-based security by introducing Spring Security filter-chain authorization and centralizing authenticated-user resolution.

## Step-by-step Plan
1. Analysis document
- Define security hardening scope and API impact.
- Output: analysis with endpoint policy and migration notes.

2. Security implementation
- Add Spring Security configuration.
- Permit only `/api/v1/auth/login` and require authentication for other API endpoints.
- Keep session-based authentication (no JWT).

3. Auth context centralization
- Add reusable component to resolve authenticated user id from session/security context.
- Refactor controllers/services to remove duplicated auth checks.

4. Regression and docs
- Update tests for security filter behavior.
- Execute `./gradlew test` and `./gradlew build`.
- Record outcomes in iteration-03 test document and result report.

5. Approval gate
- Request user approval before Iteration 04.

## Phase Policy
- Any scope addition or user-requested change is documented first.
