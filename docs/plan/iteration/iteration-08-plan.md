# Iteration 08 Plan

## Goal
Standardize security error responses so unauthorized/forbidden API access returns the same JSON error schema used by global exception handling.

## Step-by-step Plan
1. Analysis document
- Define expected 401/403 response contract.
- Output: analysis with behavior matrix.

2. Security error handlers
- Add custom AuthenticationEntryPoint (401).
- Add custom AccessDeniedHandler (403).
- Return `{ success, errorCode, message, timestamp }` JSON.

3. Security config wiring
- Register handlers in `SecurityConfig`.
- Keep current authorization rules unchanged.

4. Tests and validation
- Add/adjust tests for 401 response body fields.
- Run `./gradlew test` and `./gradlew build`.

5. Approval gate
- Request user approval before Iteration 09.

## Phase Policy
- Any additional scope is documented first.
