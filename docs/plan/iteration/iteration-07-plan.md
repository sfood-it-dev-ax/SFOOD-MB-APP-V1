# Iteration 07 Plan

## Goal
Implement local google token validation (`email + token`) and harden session lifecycle (login update, logout invalidate).

## Scope
- Require `googleToken` in login request.
- Validate existing user by exact `userId + googleToken` match.
- For first login, create user with token and initialize default board.
- On login, regenerate session and update session attributes.
- On logout, invalidate session and verify unauthorized access afterwards.
