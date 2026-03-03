# Data Spec - Iteration 01

## Common API Envelope
- Success
  - `success`: `true`
  - `data`: payload object/array
- Error
  - `success`: `false`
  - `error.code`: string
  - `error.message`: string
  - `error.path`: request URI
  - `error.timestamp`: ISO-8601

## Session Model
- Session key: `LOGIN_USER_ID`
- TTL: server default session timeout.

## Domain Models (In-Memory)

### User
- `userId` (String, unique)
- `name` (String)

### Board
- `boardId` (String, unique)
- `userId` (String, owner)
- `name` (String, 1~20)
- `isHide` (boolean)
- `createdAt` (Instant)
- `updatedAt` (Instant)

### Memo
- `memoId` (String, unique)
- `boardId` (String)
- `content` (String, nullable)
- `x` (int)
- `y` (int)
- `width` (int, >0)
- `height` (int, >0)
- `zIndex` (int)
- `memoTypeId` (String)
- `isHide` (boolean)
- `createdAt` (Instant)
- `updatedAt` (Instant)

### MemoType
- `memoTypeId` (String, unique)
- `label` (String)
- `defaultColor` (String)

## Validation
- Board name max length: 20.
- Required IDs must not be blank.
- `width`, `height` must be positive.
