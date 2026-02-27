# Iteration 11 Analysis

## Request
- Re-introduce handling of excluded `googleToken`.
- Save the token and treat "same token" check as verification.
- Avoid repeated over-scope work.

## Target Behavior
1. First login for a user:
- Verify token format/provider as current logic does.
- Persist token for the user.

2. Later login for the same user:
- Compare incoming token with persisted token.
- If same: login success.
- If different: login fail (`401 UNAUTHORIZED`).

## Scope Boundary
- This iteration only changes authentication verification behavior around `googleToken` storage/equality.
- No role/admin/security model expansion.

## Risks
- Existing real DB tests may keep previous token values.
- Integration tests need unique user IDs to avoid token collision across runs.
