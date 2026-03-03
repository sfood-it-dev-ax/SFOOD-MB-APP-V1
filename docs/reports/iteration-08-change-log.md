# Iteration 08 Change Log

## Code
- Added `src/main/java/com/sfood/mb/app/dto/board/BoardMoveRequest.java`
- Updated `src/main/java/com/sfood/mb/app/domain/Board.java`
  - added `parentBoardId`, `sortOrder`, move method
- Updated `src/main/java/com/sfood/mb/app/dto/board/BoardResponse.java`
  - added `parentBoardId`, `sortOrder`
- Updated `src/main/java/com/sfood/mb/app/application/service/BoardService.java`
  - added `moveBoard`
- Updated `src/main/java/com/sfood/mb/app/infrastructure/service/impl/BoardServiceImpl.java`
  - implemented move logic + circular reference validation
- Updated `src/main/java/com/sfood/mb/app/controller/BoardController.java`
  - added move endpoint mapping
- Updated `src/test/java/com/sfood/mb/app/integration/ApiFlowIntegrationTest.java`
  - added board move integration assertion

## Docs
- Added iteration documents:
  - `docs/plan/iteration/iteration-08-plan.md`
  - `docs/test/results/iteration-08-test-result.md`
  - `docs/reports/iteration-08-result.md`
  - `docs/reports/iteration-08-change-log.md`
