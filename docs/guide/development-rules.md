# Development Rules

## Scope
- Apply to backend code in this repository.
- Follow layered architecture: controller -> service -> repository -> domain.

## Coding Standards
- Use DTOs for request/response boundaries.
- Use constructor injection only.
- Keep business logic in service layer.
- Keep delete as logical delete via `isHide` where applicable.
- Keep APIs idempotent where retry can occur.

## Interface-First
- Define service interfaces in application package.
- Define concrete implementations in infrastructure/service.impl package.
- Depend on interfaces in controllers.

## Error Handling
- Use centralized exception handling.
- Return consistent error response schema.
- Map common non-business exceptions to 4xx (do not overuse generic 500):
  - malformed request body -> `400 VALIDATION_ERROR`
  - data integrity conflict -> `409 CONFLICT`

## Validation
- Use Bean Validation on request DTOs.
- Enforce board name max length 20.
- Explicitly name route arguments in annotations:
  - `@PathVariable("boardId")`
  - `@RequestParam("sortOrder")`

## Security
- Session-based auth only.
- No JWT.
- For Spring Session JDBC, use schema-qualified table name and aligned schema/search_path settings across active profiles.

## Observability
- Keep `/api/**` request/response logging enabled at filter level.
- Ensure backend package logging level allows API trace lines in local/dev (`INFO` or higher detail as needed).
