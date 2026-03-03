# API Spec (Frontend Integration)

## Base
- Base URL: `http://localhost:8080`
- Content-Type: `application/json`
- Auth: Session cookie (`JSESSIONID`)
- Protected path: all `/api/**` endpoints require login except `POST /api/auth/login`

## Common Response

### Success
```json
{
  "success": true,
  "data": {},
  "error": null
}
```

### Error
```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Login required",
    "path": "/api/auth/me",
    "timestamp": "2026-02-27T09:00:00Z"
  }
}
```

## Auth API

### POST `/api/auth/login`
- Description: login and create session.
- Request
```json
{
  "userId": "user-1",
  "name": "User One",
  "googleToken": "token-user-1"
}
```
- Response `200`
```json
{
  "success": true,
  "data": {
    "userId": "user-1",
    "name": "User One"
  },
  "error": null
}
```

### GET `/api/auth/me`
- Description: get current session user.
- Response `200`
```json
{
  "success": true,
  "data": {
    "userId": "user-1",
    "name": "User One"
  },
  "error": null
}
```

### POST `/api/auth/logout`
- Description: invalidate session.
- Response `200`
```json
{
  "success": true,
  "data": null,
  "error": null
}
```

## Board API

### POST `/api/boards`
- Request
```json
{
  "boardId": "board-1",
  "name": "My Board"
}
```
- Constraints
- `name` max length: 20
- Response `200`
```json
{
  "success": true,
  "data": {
    "boardId": "board-1",
    "name": "My Board",
    "isHide": false,
    "parentBoardId": null,
    "sortOrder": 0
  },
  "error": null
}
```

### GET `/api/boards`
- Response `200`
```json
{
  "success": true,
  "data": [
    {
      "boardId": "user-1_default",
      "name": "My Board",
      "isHide": false,
      "parentBoardId": null,
      "sortOrder": 0
    }
  ],
  "error": null
}
```

### PATCH `/api/boards/{boardId}/name`
- Request
```json
{
  "name": "Renamed Board"
}
```
- Response `200`
```json
{
  "success": true,
  "data": {
    "boardId": "board-1",
    "name": "Renamed Board",
    "isHide": false,
    "parentBoardId": null,
    "sortOrder": 0
  },
  "error": null
}
```

### PATCH `/api/boards/{boardId}/move`
- Request
```json
{
  "parentBoardId": "user-1_default",
  "sortOrder": 1
}
```
- Response `200`
```json
{
  "success": true,
  "data": {
    "boardId": "board-1",
    "name": "Renamed Board",
    "isHide": false,
    "parentBoardId": "user-1_default",
    "sortOrder": 1
  },
  "error": null
}
```

### DELETE `/api/boards/{boardId}`
- Response `200`
```json
{
  "success": true,
  "data": null,
  "error": null
}
```

## Memo Type API

### GET `/api/memo-types`
- Description: returns memo type catalog from DB (`memo_types`).
- Response `200`
```json
{
  "success": true,
  "data": [
    {
      "memoTypeId": "basic-yellow",
      "label": "Basic Yellow",
      "defaultColor": "#FFE66D"
    },
    {
      "memoTypeId": "basic-green",
      "label": "Basic Green",
      "defaultColor": "#C7F9CC"
    },
    {
      "memoTypeId": "basic-blue",
      "label": "Basic Blue",
      "defaultColor": "#A9DEF9"
    }
  ],
  "error": null
}
```

## Memo API

### POST `/api/memos`
- Request
```json
{
  "memoId": "memo-1",
  "boardId": "board-1",
  "memoTypeId": "basic-yellow",
  "content": "hello",
  "x": 10,
  "y": 20,
  "width": 320,
  "height": 240,
  "zIndex": 1
}
```
- Constraints
- `width >= 1`
- `height >= 1`
- Response `200`
```json
{
  "success": true,
  "data": {
    "memoId": "memo-1",
    "boardId": "board-1",
    "memoTypeId": "basic-yellow",
    "content": "hello",
    "x": 10,
    "y": 20,
    "width": 320,
    "height": 240,
    "zIndex": 1,
    "isHide": false
  },
  "error": null
}
```

### GET `/api/memos?boardId={boardId}`
- Response `200`
```json
{
  "success": true,
  "data": [
    {
      "memoId": "memo-1",
      "boardId": "board-1",
      "memoTypeId": "basic-yellow",
      "content": "hello",
      "x": 10,
      "y": 20,
      "width": 320,
      "height": 240,
      "zIndex": 1,
      "isHide": false
    }
  ],
  "error": null
}
```

### PATCH `/api/memos/{memoId}`
- Request (partial update)
```json
{
  "content": "updated",
  "x": 100,
  "y": 200,
  "width": 640,
  "height": 300,
  "zIndex": 7
}
```
- Response `200`
```json
{
  "success": true,
  "data": {
    "memoId": "memo-1",
    "boardId": "board-1",
    "memoTypeId": "basic-yellow",
    "content": "updated",
    "x": 100,
    "y": 200,
    "width": 640,
    "height": 300,
    "zIndex": 7,
    "isHide": false
  },
  "error": null
}
```

### DELETE `/api/memos/{memoId}`
- Response `200`
```json
{
  "success": true,
  "data": null,
  "error": null
}
```

## Error Codes
- `VALIDATION_ERROR` -> `400`
- `UNAUTHORIZED` -> `401`
- `INVALID_GOOGLE_TOKEN` -> `401`
- `USER_NOT_FOUND` -> `404`
- `BOARD_NOT_FOUND` -> `404`
- `MEMO_NOT_FOUND` -> `404`
- `MEMO_TYPE_NOT_FOUND` -> `404`
- `CONFLICT` -> `409`
- `INTERNAL_ERROR` -> `500`

## Frontend Notes
- Browser must send cookies (`credentials: 'include'` for fetch / `withCredentials: true` for axios).
- Deletions are logical deletes on backend.
- Use IDs generated by frontend for `boardId` and `memoId`.
