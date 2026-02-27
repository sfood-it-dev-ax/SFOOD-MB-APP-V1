# Data Spec (Iteration 01)

## API Response Schema
- Success: `{ success, data, message, timestamp }`
- Error: `{ success, errorCode, message, timestamp }`

## Core Entities
- User: `userId`, `email`, `name`, `profileImage`, `isHide`
- Board: `boardId`, `userId`, `parentBoardId`, `boardName`, `sortOrder`, `isHide`
- Memo: `memoId`, `boardId`, `typeId`, `content`, `posX`, `posY`, `width`, `height`, `zIndex`, `isHide`
- MemoType: `typeId`, `typeName`, `defaultColor`, `shapeCss`, `sortOrder`, `isActive`

## Validation Rules
- `boardName`: 1..20 chars.
- `boardId`, `memoId`, `typeId`: non-blank.
- `zIndex`, `sortOrder`: integer.
