# Iteration 07 Change Log

## Code
- Updated `src/main/java/com/sfood/mb/app/dto/auth/LoginRequest.java`
  - Added required field: `googleToken`.
- Updated `src/main/java/com/sfood/mb/app/domain/User.java`
  - Added persisted field: `googleToken`.
  - Added update/bind helper methods.
- Updated `src/main/java/com/sfood/mb/app/infrastructure/service/impl/AuthServiceImpl.java`
  - Added local token match validation and mismatch rejection (`INVALID_GOOGLE_TOKEN`).
- Updated `src/main/java/com/sfood/mb/app/controller/AuthController.java`
  - Login now invalidates old session, creates new session, and sets session attributes.
- Updated `src/main/java/com/sfood/mb/app/config/SessionConstants.java`
  - Added `LOGIN_USER_NAME`, `LOGIN_AT`.

## Tests
- Updated `src/test/java/com/sfood/mb/app/integration/ApiFlowIntegrationTest.java`
  - Added `googleToken` in login payload.
- Added `src/test/java/com/sfood/mb/app/integration/AuthTokenSessionIntegrationTest.java`
  - Added token mismatch and session lifecycle verification.
