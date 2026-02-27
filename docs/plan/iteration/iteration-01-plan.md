# Iteration 01 Plan

## Goal
Implement backend core APIs aligned to `requirements.md` and `architectures.md` with a document-first workflow.

## Step-by-step Plan
1. Documentation baseline
- Create missing guide/product/done docs required by AGENTS rules.
- Output: baseline docs created.

2. Analysis document
- Define concrete API scope for Iteration 01 and implementation assumptions.
- Output: analysis report with endpoint list and risk notes.

3. Backend implementation (Phase A)
- Common API response and global exception handler.
- Session auth APIs: login/logout/me (mock token parsing for local development).
- Board APIs: list/create/update-name/move/delete (logical delete).
- Memo APIs: list/create/update-content/update-position/update-size/update-zindex/delete.
- Memo type API: list active types.
- Output: runnable Spring Boot code + tests.

4. Test documentation and execution
- Create test result document.
- Run `./gradlew test` and `./gradlew build`.
- Record pass/fail and key logs.

5. Iteration result documentation
- Summarize delivered scope, design decisions, and known gaps.

6. Approval gate
- After all tests pass, request user approval before Iteration 02 planning/execution.

## Phase Policy
- Any out-of-plan change or new request must be documented in analysis/result/test docs before implementation.
