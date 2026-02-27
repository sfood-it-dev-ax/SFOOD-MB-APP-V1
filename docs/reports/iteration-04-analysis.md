# Iteration 04 Analysis

## Objective
Improve auth reliability by introducing an interface-first Google token verification component and decoupling token parsing from `AuthService`.

## Scope
- Add `GoogleTokenVerifier` interface and DTO for verified identity.
- Add verifier implementations:
  - Local verifier: deterministic parsing for local/test.
  - External verifier: calls Google token-info endpoint.
- Add config-based switch (`app.auth.google-verifier-mode`).
- Refactor `AuthServiceImpl` to use verifier interface.

## Behavior Policy
- Local mode default: preserves current test/dev behavior.
- External mode: invalid or unverifiable tokens return `400 VALIDATION_ERROR`.

## Risks
- External HTTP dependency can fail by network/latency.
- Mode misconfiguration can break login unexpectedly.

## Mitigation
- Keep default mode local.
- Validate mode values and fail fast with clear message.
