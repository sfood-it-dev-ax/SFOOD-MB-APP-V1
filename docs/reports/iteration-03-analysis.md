# Iteration 03 Analysis

## Objective
Apply framework-level API access control with Spring Security while preserving session-based authentication requirements.

## Scope
- Add Spring Security dependency and filter chain configuration.
- Authorization policy:
  - Permit: `POST /api/v1/auth/login`
  - Require authentication: all other `/api/**`
- Keep existing session login behavior.
- Keep API contracts unchanged.

## Refactoring Plan
- Introduce `SessionUserResolver` for authenticated user-id lookup.
- Replace repeated direct session-access code in controllers with resolver usage.

## Risks
- Security defaults may block existing tests unexpectedly.
- CSRF behavior can affect non-browser test requests.

## Mitigation
- Disable CSRF for API-only flow in this iteration.
- Keep integration test flow as baseline regression check.
