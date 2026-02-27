# Iteration 05 Plan

## Goal
Harden session handling by moving to Spring Session JDBC storage and explicit session-cookie security settings.

## Step-by-step Plan
1. Analysis document
- Define target session storage behavior and test impact.
- Output: analysis with config changes and risk notes.

2. Session JDBC migration
- Add Spring Session JDBC dependency.
- Enable JDBC session store and schema initialization.
- Keep existing login/logout/session API behavior unchanged.

3. Security/session options
- Add explicit session cookie options (http-only, same-site, timeout).
- Ensure API behavior remains compatible with existing tests.

4. Regression tests and docs
- Add/adjust tests for unauthorized access and session behavior.
- Run `./gradlew test` and `./gradlew build`.
- Record logs in iteration-05 test/result docs.

5. Approval gate
- Request user approval before Iteration 06.

## Phase Policy
- Out-of-plan change requests must be documented first.
