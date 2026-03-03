# Iteration 01 Plan

## Scope
- Auth session API (`/api/auth/login`, `/api/auth/logout`, `/api/auth/me`)
- Board API (`/api/boards` create/list/update-name/delete)
- Memo API (`/api/memos` create/list/update/delete)
- Memo type API (`/api/memo-types` list)
- Global error handling and API logging filter
- Test docs and iteration result docs

## Implementation Plan
1. Define DTOs, response envelope, exception/error contract.
2. Implement interface-first services and in-memory repositories.
3. Build controllers with Bean Validation and explicit route argument names.
4. Add `/api/**` logging filter and centralized exception handler.
5. Write service unit tests and WebMvc tests for required flows.
6. Run `./gradlew test`, `./gradlew build`, and document outputs.

## Risks
- In-memory persistence is non-durable; behavior is limited to runtime.
- Session tests depend on MockMvc session propagation.

## Done Target
- All required test flows pass and are documented.
