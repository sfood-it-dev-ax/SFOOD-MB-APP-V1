# Iteration 04 Plan

## Goal
Strengthen authentication by separating Google token verification into an interface-first component with environment-specific implementations.

## Step-by-step Plan
1. Analysis document
- Define verification policy and behavior for local/test vs production-like mode.
- Output: analysis with interface and error-handling plan.

2. Token verifier architecture
- Add `GoogleTokenVerifier` interface.
- Add local parser implementation for development/testing.
- Add external verifier implementation for real Google token introspection endpoint.

3. Auth integration
- Refactor `AuthService` login flow to depend on verifier interface only.
- Keep session issuance and default-board bootstrap behavior unchanged.

4. Configuration
- Add property-driven mode switch for verifier selection.
- Keep safe local default to avoid test flakiness.

5. Validation and docs
- Execute `./gradlew test` and `./gradlew build`.
- Record logs and outcomes in iteration-04 test/result docs.

6. Approval gate
- Request user approval before Iteration 05.

## Phase Policy
- Any additional request/change is documented first.
