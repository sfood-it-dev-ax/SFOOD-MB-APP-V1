# Iteration 06 Change Log

## Code
- Updated `build.gradle`
  - added `org.springframework.boot:spring-boot-starter-aop`
- Added `src/main/java/com/sfood/mb/app/logging/ControllerApiLoggingAspect.java`

## Behavior
- New logs emitted on controller method entry/exit/error for all `@RestController` handlers.
