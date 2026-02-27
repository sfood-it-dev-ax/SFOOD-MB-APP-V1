# Iteration 04 Result

## Delivered Scope
- Added interface-first Google token verification contract:
  - `GoogleTokenVerifier`
  - `VerifiedGoogleUser`
- Added verifier implementations:
  - `LocalGoogleTokenVerifier` (`app.auth.google-verifier-mode=local`)
  - `ExternalGoogleTokenVerifier` (`app.auth.google-verifier-mode=external`)
- Refactored `AuthServiceImpl` to depend on verifier interface only.
- Added auth properties in `application.properties` for verifier mode and token-info URL.
- Added `LocalGoogleTokenVerifierTest` unit test.

## Key Decisions
- Local mode remains default for stable local/test behavior.
- External mode returns validation error on invalid/unverifiable token.

## Remaining Gaps
- External verifier integration is code-ready, but runtime network/credential hardening and e2e validation against real Google OAuth issuance is still needed.
