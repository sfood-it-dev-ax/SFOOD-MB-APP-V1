# PRD - Post-it Board (Iteration 01 실행형)

## 문서 정보
- 버전: v1.2
- 수정일: 2026-03-03
- 상태: Active
- 기준 문서:
- `docs/guide/requirements.md`
- `docs/guide/architectures.md`
- `docs/guide/development-rules.md`
- `docs/guide/testing-strategy.md`
- `docs/guide/done-criteria.md`

## 이번 수정 목적
- `requirements`와 `architectures` 기준으로 API/기능 누락을 재점검하고 PRD를 보완한다.
- API 개발, PostgreSQL 실데이터 테스트, `api-spec` 결과 기록을 한 흐름으로 관리한다.

## 범위
- 포함: 인증, 보드, 메모, 메모 타입 API 및 연관 기능
- 제외: JWT, ID/PW 로그인, 검색/정렬, 파일 첨부, Import/Export

## 인증 정책 (Iteration 01 구현 정책)
- 로그인 입력은 프론트 전달값 `googleAccount`, `token`을 사용한다.
- 서버는 외부 Google API 호출 없이 전달값 존재/기본 형식/사용자 존재 여부만 검증한다.
- 기존 사용자면 세션 로그인, 신규면 사용자 + 기본 보드 생성 후 세션 로그인 처리한다.
- 세션 만료 시간은 24시간으로 유지한다.

## 테스트 및 문서화 정책
- PostgreSQL DB를 생성하고 실제 API 요청 데이터를 전송해 검증한다.
- API 테스트 완료 시 아래 3가지를 동시에 수행한다.
- PRD 체크 상태 업데이트
- `docs/product/api-spec.md`에 요청/응답/DB 반영 결과 기록
- `docs/test/results/iteration-01-test-result.md`에 실행 명령과 결과 기록

## 기능 단위 실행 계획

### Feature 1. 인증(Auth)
- 목표: 로그인/로그아웃/세션조회 완성
- 완료 기준: Auth API 3종 개발 + PostgreSQL 테스트 + api-spec 기록 완료

### Feature 2. 보드(Board)
- 목표: 보드 트리 조회, 생성, 이름수정, 이동, 삭제(논리삭제) 완성
- 완료 기준: Board API 5종 개발 + PostgreSQL 테스트 + api-spec 기록 완료

### Feature 3. 메모(Memo)
- 목표: 메모 CRUD 및 내용/위치/크기/z-index 변경 완성
- 완료 기준: Memo API 7종 개발 + PostgreSQL 테스트 + api-spec 기록 완료

### Feature 4. 메모 타입(Memo Types)
- 목표: 메모 타입 목록 조회 API 완성
- 완료 기준: Memo Type API 1종 개발 + PostgreSQL 테스트 + api-spec 기록 완료

### Feature 5. 자동 저장/오류 UX 연동
- 목표: 자동 저장(1초), 실패 시 오류 응답 규약/롤백 연동 기준 확정
- 완료 기준: 관련 API 멱등성 및 실패 응답 규약 문서화

### Feature 6. 비API 기능 점검
- 목표: 보드 줌, LNB 리사이즈, 편집/완료 모드 전환 조건 누락 없이 반영
- 완료 기준: 요구사항 매핑 표 기준 누락 없음 확인

## API 인벤토리 (architectures 기준)
- Base URL: `/api/v1`

### Auth API
| API | 설명 | REQ 매핑 | 개발 | PostgreSQL 실데이터 테스트 | api-spec 결과 기록 |
| --- | --- | --- | --- | --- | --- |
| `POST /api/v1/auth/login` | `googleAccount`, `token` 기반 로그인/신규 생성/세션 발급 | AUTH-001~007 | [x] | [x] | [x] |
| `POST /api/v1/auth/logout` | 세션 만료 처리 | AUTH-009~011 | [x] | [x] | [x] |
| `GET /api/v1/auth/me` | 현재 세션 사용자 조회 | AUTH-007 | [x] | [x] | [x] |

### Board API
| API | 설명 | REQ 매핑 | 개발 | PostgreSQL 실데이터 테스트 | api-spec 결과 기록 |
| --- | --- | --- | --- | --- | --- |
| `GET /api/v1/boards` | 사용자 보드 트리 조회 (`is_hide=false`) | BOARD-001~003 | [x] | [x] | [x] |
| `POST /api/v1/boards` | 보드 생성 | BOARD-004~008 | [x] | [x] | [x] |
| `PATCH /api/v1/boards/{boardId}/name` | 보드명 수정 (20자 제한) | BOARD-009~011 | [x] | [x] | [x] |
| `PATCH /api/v1/boards/{boardId}/move` | 보드 위치/계층 이동 | BOARD-015 | [x] | [x] | [x] |
| `DELETE /api/v1/boards/{boardId}` | 보드 논리 삭제 (`is_hide=true`) | BOARD-012~014 | [x] | [x] | [x] |

### Memo API
| API | 설명 | REQ 매핑 | 개발 | PostgreSQL 실데이터 테스트 | api-spec 결과 기록 |
| --- | --- | --- | --- | --- | --- |
| `GET /api/v1/boards/{boardId}/memos` | 보드 메모 조회 (`is_hide=false`, z_index 순) | MEMO-001~005 | [x] | [x] | [x] |
| `POST /api/v1/boards/{boardId}/memos` | 메모 생성 (타입/초기 위치 포함) | MEMO-006~011 | [x] | [x] | [x] |
| `PATCH /api/v1/memos/{memoId}/content` | 메모 내용 수정 | MEMO-012~013 | [x] | [x] | [x] |
| `PATCH /api/v1/memos/{memoId}/position` | 메모 위치 수정 | MEMO-014 | [x] | [x] | [x] |
| `PATCH /api/v1/memos/{memoId}/size` | 메모 크기 수정 | MEMO-015 | [x] | [x] | [x] |
| `PATCH /api/v1/boards/{boardId}/memos/zindex` | 메모 노출순서 일괄 수정 | MEMO-016~018 | [x] | [x] | [x] |
| `DELETE /api/v1/memos/{memoId}` | 메모 논리 삭제 (`is_hide=true`) | MEMO-019~021 | [x] | [x] | [x] |

### Memo Type API
| API | 설명 | REQ 매핑 | 개발 | PostgreSQL 실데이터 테스트 | api-spec 결과 기록 |
| --- | --- | --- | --- | --- | --- |
| `GET /api/v1/memo-types` | 활성 메모 타입 목록 조회 (`is_active=true`) | MEMO-024 | [x] | [x] | [x] |

## 비API 기능 체크리스트 (누락 점검)
| 기능 | REQ 매핑 | 상태 |
| --- | --- | --- |
| 자동 저장 1초 딜레이 + 실패 롤백 | AUTO-001~004, NFR-002, NFR-004 | [ ] |
| 보드 줌(Ctrl+Wheel) | UI-001 | [ ] |
| LNB 리사이즈 | BOARD-003 | [ ] |
| 에디터 툴바 노출 조건/기능 | MEMO-022~023 | [ ] |

## 문서 산출물 규칙
- `docs/product/api-spec.md`:
- API별 요청/응답 예시
- 테스트 payload
- PostgreSQL 반영 결과(핵심 컬럼)
- 성공/실패 케이스 결과
- `docs/test/results/iteration-01-test-result.md`:
- 실행 명령/시간/성공여부
- 실패 원인 및 재실행 결과

## 이번 점검 결과
- [x] `memo-types` API 누락 보완
- [x] Board API를 `name`/`move` 엔드포인트로 분리 반영
- [x] Memo API를 `content`/`position`/`size`/`zindex`로 분리 반영
- [x] 기능 누락 점검용 비API 체크리스트 추가
- [x] API 구현 및 테스트 진행
- [x] api-spec 결과 기록 완료
