# Done Criteria

## Mandatory
- Plan document exists for the iteration.
- Analysis document exists and matches implemented scope.
- Code and tests are implemented for planned scope.
- Test result document includes commands and pass/fail.
- Result report includes delivered features, decisions, and gaps.

## Quality Gates
- All tests pass.
- API response format is consistent.
- No intentional silent failure paths.
- `/api/**` request/response logs are observable in local/dev runs.
- Unhandled exception logs include request method/URI and stacktrace.
- API error contract includes and verifies `400/401/404/409` branches where applicable.
- Runtime profile/JDK/env assumptions are documented when behavior differs between IDE and CLI execution.
