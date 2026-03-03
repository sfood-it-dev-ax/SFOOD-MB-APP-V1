# Claude - 아키텍처 설계서

> **Post-it Board — 개인 메모 웹앱**
>

| 항목 | 내용 |
| --- | --- |
| 문서 번호 | ARCH-MEMO-2025-001 |
| 프로젝트명 | Post-it Board — 개인 메모 웹앱 |
| SRS 기준 | SRS-MEMO-2025-001 v1.0 |
| 문서 버전 | v1.1 |
| 작성일 | 2025.02.25 |
| 수정일 | 2025.02.26 |
| Frontend 서버 | SFOOD-MB-WEB-V1 (Local) |
| Backend 서버 | SFOOD-MB-APP-V1 (Local) |
| 상태 | 수정 (Revised) |

---

## 📌 목차

- 시스템 아키텍처 개요
- Frontend 아키텍처
- Backend 아키텍처
- 데이터베이스 설계
- REST API 설계
- 보안 설계
- 환경 설정
- 개발 우선순위 및 구현 순서

---

## 🗺 시스템 아키텍처 개요

### 아키텍처 원칙

본 시스템은 SRS에 명세된 요구사항을 기반으로 아래 원칙에 따라 설계한다.

- Frontend / Backend 완전 분리 구조 (Decoupled Architecture)
- REST API 기반 통신, JSON 포맷 사용
- 서버 세션 기반 인증 **(JWT 미사용 — SRS 제약 조건 준수)**
- 데이터 논리 삭제 원칙 (is_hide 컬럼 방식 — 물리 DELETE 미사용)
- 자동 저장 메커니즘 — 클라이언트 완료 모드 전환 시 1초 딜레이 후 API 호출
- SOLID 원칙 기반 레이어드 아키텍처 (Controller → Service → Repository)

### 전체 아키텍처 다이어그램

```
┌─────────────────────────────────────────────────────────────┐
│                      BROWSER (Client)                       │
│               React SPA  |  Vite Build                      │
│       Component Layer /  State Management (Zustand)         │
│     Rich Text Editor | Drag & Drop Engine | Canvas Zoom     │
│         Google OAuth 2.0 Client | HTTP Client (Axios)       │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/HTTPS | REST API | JSON
┌──────────────────────────▼──────────────────────────────────┐
│                    API SERVER (Backend)                      │
│            Spring Boot 3.5 | JDK 21 | Gradle 8.1           │
│          Controller Layer / Service Layer / Repository Layer │
│            Spring Session (Server-side) | Google OAuth2     │
│               JPA / Hibernate | QueryDSL | Validation       │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC | JPA | HikariCP
┌──────────────────────────▼──────────────────────────────────┐
│               DATABASE (PostgreSQL — Supabase)              │
│    Schema: memo_board_v3  |  aws-1-ap-south-1.pooler.supabase.com:6543  │
│           Tables: users / boards / memos / memo_types       │
│            논리 삭제 (is_hide) | UTF-8 charset              │
└─────────────────────────────────────────────────────────────┘
```

### 시스템 구성 요약

| 구분 | 서버명 | 언어/프레임워크 | 버전 | Git Repository                  |
| --- | --- | --- | --- |---------------------------------|
| Frontend | SFOOD-MB-WEB-V1 | Vite + React | AI 선택 | sfood-it-dev-ax/SFOOD-MB-WEB-V1 |
| Backend | SFOOD-MB-APP-V1 | Java Spring Boot | JDK 21 / Spring 3.5 | sfood-it-dev-ax/SFOOD-MB-APP-V1 |
| Database | aws-1-ap-south-1.pooler.supabase.com:6543 | PostgreSQL (Supabase) | 15+ | Schema: memo_board_v3           |

---

## 💻 Frontend 아키텍처

### 기술 스택

| 분류 | 기술/라이브러리 | 용도 |
| --- | --- | --- |
| 빌드 도구 | Vite | HMR, 빠른 개발 서버, 번들링 |
| UI 프레임워크 | React (SPA) | 컴포넌트 기반 UI 구성 |
| 언어 | TypeScript | 타입 안전성 확보 |
| 라우팅 | React Router v7 | 페이지 라우팅 (로그인 / 메인 화면) |
| 상태 관리 | Zustand 또는 Context API | 보드/메모 전역 상태 관리 |
| HTTP 통신 | Axios | REST API 호출, 인터셉터 기반 에러 처리 |
| Rich Text 에디터 | TipTap 또는 Quill | 메모 에디터 툴바 (색상/폰트/체크박스/리스트) |
| 드래그 앤 드롭 | react-dnd 또는 dnd-kit | 메모 위치 이동, 보드 트리 드래그 |
| UI 컴포넌트 | Tailwind CSS | 스타일링 |
| 인증 | Google OAuth 2.0 Client | 구글 계정 연동 로그인 |

### 디렉토리 구조

```
SFOOD-MB-WEB-V1/
├── src/
│   ├── assets/                    # 정적 리소스 (이미지, 폰트)
│   ├── components/
│   │   ├── board/                 # LNB 트리, 보드 패널 컴포넌트
│   │   ├── memo/                  # 메모 카드, 에디터 툴바, 메모 툴바
│   │   └── common/                # 모달, 팝업, 공통 UI 컴포넌트
│   ├── pages/
│   │   ├── LoginPage.tsx          # 로그인 화면 — 구글 OAuth 버튼 처리
│   │   └── MainPage.tsx           # 메인 화면 — LNB + 보드 영역 레이아웃
│   ├── hooks/                     # 커스텀 훅 (useBoard, useMemo, useAutoSave)
│   ├── store/                     # Zustand 스토어 (boardStore, memoStore, authStore)
│   ├── api/                       # API 모듈 (auth.api.ts, board.api.ts, memo.api.ts)
│   ├── types/                     # TypeScript 타입 정의 (Board, Memo, MemoType 등)
│   ├── utils/                     # 유틸 함수 (ID 생성, 날짜 포맷, 에러 핸들러)
│   ├── router/                    # React Router 설정, Route Guard
│   └── constants/                 # 상수 정의 (API URL, 딜레이 타임 등)
├── public/                        # 정적 파일 서빙 루트
├── index.html                     # SPA 진입점
├── vite.config.ts                 # Vite 설정 (Proxy, alias)
└── tsconfig.json                  # TypeScript 컴파일러 설정
```

### 핵심 컴포넌트 설계

| 컴포넌트 | 역할 | 주요 Props / State | 관련 REQ |
| --- | --- | --- | --- |
| `BoardTree` | LNB 보드 트리 렌더링, 보드 CRUD 이벤트 처리 | boards[], selectedBoardId | BOARD-001~015 |
| `BoardCanvas` | 선택된 보드 메모 영역, 줌 처리 | memos[], zoom, boardId | MEMO-001~005, UI-001 |
| `MemoCard` | 개별 메모 컴포넌트 (뷰/편집 모드) | memo, mode, onSave, onDelete | MEMO-006~021 |
| `EditorToolbar` | 메모 편집 모드 서식 툴바 | editorInstance, onFormat | MEMO-022~023 |
| `MemoToolbar` | 하단 메모 타입 선택 툴바 | memoTypes[], onSelect | MEMO-024 |
| `ContextMenu` | 메모 우클릭 노출순서 메뉴 | position, onOrder | MEMO-016~017 |
| `LoginPage` | 구글 OAuth 로그인 화면 | linkedAccount | AUTH-001~008 |
| `useAutoSave` | 완료 모드 전환 시 1초 딜레이 저장 훅 | saveFn, delay=1000 | AUTO-001~004 |

### 상태 관리 설계

| 스토어 | 관리 상태 | 주요 액션 |
| --- | --- | --- |
| `authStore` | user, session, isAuthenticated | login(), logout(), checkSession() |
| `boardStore` | boards[], selectedBoardId, treeData | fetchBoards(), addBoard(), updateBoard(), deleteBoard(), moveBoard() |
| `memoStore` | memos[], editingMemoId | fetchMemos(), addMemo(), updateMemo(), deleteMemo(), reorderMemos() |
| `uiStore` | zoom, lnbWidth, contextMenuState | setZoom(), setLnbWidth(), openContextMenu() |

### ID 자동 생성 유틸

> ⚠️ 보드/메모 ID는 클라이언트에서 자동 생성한다. 숫자·영문·특수문자 포함 10자리 랜덤 문자열로 구성한다. (SRS DATA-001, DATA-002)
>

```tsx
// src/utils/idGenerator.ts
const CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";

export function generateId(accountId: string, parentId?: string): string {
  const random = Array.from({ length: 10 },
    () => CHARS[Math.floor(Math.random() * CHARS.length)]).join("");
  return parentId ? `${parentId}_${random}` : `${accountId}_${random}`;
}

// 보드 ID 예시: dhlee@gmail.com_10@9eikf
// 메모 ID 예시: dhlee@gmail.com_10@9eikf_a!di2$3
```

### 자동 저장 훅

```tsx
// src/hooks/useAutoSave.ts
const useAutoSave = (saveFn: () => Promise<void>, delay = 1000) => {
  const timerRef = useRef<ReturnType<typeof setTimeout>>();

  const triggerSave = useCallback(() => {
    clearTimeout(timerRef.current);
    timerRef.current = setTimeout(async () => {
      try {
        await saveFn();                              // AUTO-002: 완료 모드 전환 시 저장
      } catch (e) {
        rollback();                                  // AUTO-004: 실패 시 롤백
        showErrorToast("저장에 실패했습니다.");
      }
    }, delay);                                       // AUTO-003: 기본 1초 딜레이
  }, [saveFn, delay]);

  return { triggerSave };
};

// 완료 모드 전환 시 호출
const handleBlur = () => {
  setMode("complete");
  triggerSave();
};
```

---

## ⚙️ Backend 아키텍처

### 기술 스택

| 분류 | 기술/라이브러리 | 버전 | 용도 |
| --- | --- | --- | --- |
| 언어 | Java | JDK 21 | 기본 개발 언어 (Virtual Threads 지원) |
| 프레임워크 | Spring Boot | 3.5 | REST API 서버 기반 |
| 빌드 도구 | Gradle | 8.1 | 의존성 관리, 빌드 |
| ORM | Spring Data JPA / Hibernate | 최신 | 엔티티-DB 매핑, CRUD |
| 쿼리 | QueryDSL | 최신 | 동적 쿼리 작성 |
| 인증 | Spring Security + OAuth2 Client | 최신 | 구글 OAuth2 연동, 세션 기반 인증 |
| 세션 | Spring Session | 최신 | 서버 세션 저장/관리 (24시간 만료) |
| 연결 풀 | HikariCP | 내장 | DB 커넥션 풀 관리 |
| 유효성 검사 | Spring Validation | 내장 | Request DTO 검증 |
| 코드 간소화 | Lombok | 최신 | Boilerplate 코드 최소화 |

### 레이어드 아키텍처

> SOLID 원칙을 준수하는 레이어드 아키텍처. 각 레이어는 단방향 의존성을 가진다.
>

```
[ Controller ]  ← HTTP 요청/응답 처리, DTO 변환, 파라미터 검증
      ↓
[ Service ]     ← 비즈니스 로직, 트랜잭션 관리, 도메인 규칙 적용
      ↓
[ Repository ]  ← DB 접근, JPA/QueryDSL 쿼리 실행
      ↓
[ Domain ]      ← JPA Entity, 연관 관계 매핑
```

| 레이어 | 책임 | 주요 클래스 |
| --- | --- | --- |
| Controller | HTTP 요청/응답 처리, DTO 변환, 파라미터 검증 | `AuthController`, `BoardController`, `MemoController` |
| Service | 비즈니스 로직, 트랜잭션 관리, 도메인 규칙 적용 | `AuthService`, `BoardService`, `MemoService` |
| Repository | DB 접근, JPA/QueryDSL 쿼리 실행 | `BoardRepository`, `MemoRepository`, `UserRepository` |
| Domain/Entity | 도메인 모델 정의, 연관 관계 매핑 | `User`, `Board`, `Memo`, `MemoType` |
| DTO | 계층간 데이터 전달 객체 (Request/Response 분리) | `BoardRequest`, `MemoResponse`, `LoginRequest` 등 |
| Config | 스프링 설정, 보안 설정, CORS 설정 | `SecurityConfig`, `SessionConfig`, `CorsConfig` |
| Exception | 전역 예외 처리, 공통 에러 응답 포맷 | `GlobalExceptionHandler`, `CustomException` |

### 디렉토리 구조

```
SFOOD-MB-APP-V1/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sfood/memoboard/
│   │   │       ├── config/        # SecurityConfig, CorsConfig, SessionConfig, JpaConfig
│   │   │       ├── controller/    # AuthController, BoardController, MemoController
│   │   │       ├── service/       # 인터페이스 + 구현체 (AuthService, BoardService, MemoService)
│   │   │       ├── repository/    # UserRepository, BoardRepository, MemoRepository, MemoTypeRepository
│   │   │       ├── domain/        # User, Board, Memo, MemoType (JPA Entity)
│   │   │       ├── dto/
│   │   │       │   ├── request/   # 입력 DTO
│   │   │       │   └── response/  # 출력 DTO
│   │   │       ├── exception/     # CustomException, GlobalExceptionHandler, ErrorResponse
│   │   │       ├── util/          # 공통 유틸 클래스
│   │   │       └── MemoboardApplication.java
│   │   └── resources/
│   │       ├── application.yml            # 공통 설정
│   │       └── application-local.yml      # 로컬 환경 설정 (DB 접속 정보 등)
│   └── test/
│       └── java/                          # 테스트 소스
├── build.gradle                           # Gradle 빌드 설정
└── settings.gradle
```

### 세션 인증 흐름

> ⚠️ **JWT를 사용하지 않는다.** Spring Session 기반 서버 세션으로 인증 상태를 관리한다. 세션 유효 시간은 **24시간**이다. (SRS AUTH-006, NFR-005)
>

```
1. Client → Google OAuth2 → 구글 인증 서버 → Authorization Code 반환
2. Client → POST /api/v1/auth/login { googleToken } → API Server
3. API Server → Google API 토큰 검증 → DB User 조회 / 없으면 자동 생성
4. API Server → Spring Session 생성 (sessionId, TTL = 24h)
5. API Server → Response: Set-Cookie: SESSION=<sessionId>; HttpOnly; SameSite=Lax
6. 이후 모든 API 호출 → Cookie 자동 첨부 → Spring Security 세션 검증
7. 세션 만료 시 → 401 Unauthorized → Client 로그인 화면 이동 + 만료 메시지 표시
```

### build.gradle 주요 의존성

```groovy
dependencies {
    // Spring Boot
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
    implementation 'org.springframework.boot:spring-boot-starter-validation'

    // Spring Session
    implementation 'org.springframework.session:spring-session-jdbc'

    // QueryDSL
    implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
    annotationProcessor 'com.querydsl:querydsl-apt:5.0.0:jakarta'

    // PostgreSQL
    runtimeOnly 'org.postgresql:postgresql'

    // Lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // Test
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
}
```

---

## 🗄 데이터베이스 설계

### DB 환경 정보

| 항목 | 값                                                                                           |
| --- |---------------------------------------------------------------------------------------------|
| RDBMS | PostgreSQL (Supabase Managed)                                                               |
| 서버 | aws-1-ap-south-1.pooler.supabase.com                                                        |
| 포트 | 6543                                                                                        |
| 스키마(DB) | memo_board_v3                                                                               |
| 접속 URL | `jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/memo_board_v3?sslmode=require` |
| 계정 ID | postgres.wcvyfovaiigrkmzsqrpq                                                               |
| Character Set | UTF-8 (PostgreSQL 기본)                                                                       |
| Collation | en_US.UTF-8                                                                                 |

> ⚠️ Supabase Transaction Pooler(PgBouncer) 포트 **6543** 사용. SSL 연결 필수(`sslmode=require`).
>

### ERD 관계 구조

```
[ users ]
     | 1
     |
     | N
[ boards ] ◄─── 자기 참조 (parent_board_id) ─── 트리 구조
     | 1
     |
     | N
[ memos ] ──── FK: board_id
     | N           |
     |             | N:1
     |             ▼
     └──────► [ memo_types ] (메모 모양/기본색상 마스터)

※ 모든 삭제: is_hide = TRUE (논리 삭제)
※ 모든 조회: WHERE is_hide = FALSE 조건 필수
```

### TABLE: users

| 컬럼명 | 타입 | NULL | KEY | 설명 |
| --- | --- | --- | --- | --- |
| `user_id` | VARCHAR(255) | NOT NULL | PK | 구글 계정 이메일 (Primary Key) |
| `email` | VARCHAR(255) | NOT NULL |  | 구글 계정 이메일 |
| `name` | VARCHAR(100) | NOT NULL |  | 구글 계정 표시 이름 |
| `profile_image` | VARCHAR(500) | NULL |  | 구글 프로필 이미지 URL |
| `created_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 계정 생성 일시 |
| `updated_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 마지막 수정 일시 |
| `is_hide` | BOOLEAN | NOT NULL |  | 논리 삭제 여부 (FALSE: 정상, TRUE: 삭제) |

### TABLE: boards

| 컬럼명 | 타입 | NULL | KEY | 설명 |
| --- | --- | --- | --- | --- |
| `board_id` | VARCHAR(255) | NOT NULL | PK | 보드 고유 ID (클라이언트 생성: 계정_랜덤10자리) |
| `user_id` | VARCHAR(255) | NOT NULL | FK | users.user_id 참조 |
| `parent_board_id` | VARCHAR(255) | NULL | FK | 상위 보드 ID (최상위 보드는 NULL) |
| `board_name` | VARCHAR(20) | NOT NULL |  | 보드 이름 (최대 20자) |
| `sort_order` | INTEGER | NOT NULL |  | 같은 레벨 내 정렬 순서 |
| `created_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 생성 일시 |
| `updated_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 수정 일시 |
| `is_hide` | BOOLEAN | NOT NULL |  | 논리 삭제 여부 (FALSE: 정상, TRUE: 삭제) |

### TABLE: memo_types

| 컬럼명 | 타입 | NULL | KEY | 설명 |
| --- | --- | --- | --- | --- |
| `type_id` | VARCHAR(50) | NOT NULL | PK | 메모 타입 고유 ID (예: `TYPE_BASIC`, `TYPE_ROUND`) |
| `type_name` | VARCHAR(50) | NOT NULL |  | 메모 타입 이름 |
| `default_color` | VARCHAR(20) | NOT NULL |  | 기본 배경색 (HEX 코드) |
| `shape_css` | VARCHAR(255) | NOT NULL |  | 모양 CSS 클래스명 또는 스타일 |
| `sort_order` | INTEGER | NOT NULL |  | 툴바 노출 순서 |
| `is_active` | BOOLEAN | NOT NULL |  | 활성 여부 |

### TABLE: memos

| 컬럼명 | 타입 | NULL | KEY | 설명 |
| --- | --- | --- | --- | --- |
| `memo_id` | VARCHAR(255) | NOT NULL | PK | 메모 고유 ID (클라이언트 생성: 보드ID_랜덤10자리) |
| `board_id` | VARCHAR(255) | NOT NULL | FK | boards.board_id 참조 |
| `type_id` | VARCHAR(50) | NOT NULL | FK | memo_types.type_id 참조 |
| `content` | TEXT | NULL |  | 메모 내용 (Rich Text HTML 또는 JSON) |
| `pos_x` | REAL | NOT NULL |  | 보드 내 X 좌표 (px) |
| `pos_y` | REAL | NOT NULL |  | 보드 내 Y 좌표 (px) |
| `width` | REAL | NOT NULL |  | 메모 너비 (px) |
| `height` | REAL | NOT NULL |  | 메모 높이 (px) |
| `z_index` | INTEGER | NOT NULL |  | 노출 순서 (높을수록 앞에 표시) |
| `created_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 생성 일시 |
| `updated_at` | TIMESTAMP WITH TIME ZONE | NOT NULL |  | 수정 일시 |
| `is_hide` | BOOLEAN | NOT NULL |  | 논리 삭제 여부 (FALSE: 정상, TRUE: 삭제) |

### DDL 스크립트 (PostgreSQL)

```sql
-- 스키마 생성
CREATE SCHEMA IF NOT EXISTS memo_board_v3;
SET search_path TO memo_board_v3;

-- updated_at 자동 갱신 트리거 함수 (MySQL ON UPDATE CURRENT_TIMESTAMP 대체)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- users
CREATE TABLE users (
    user_id         VARCHAR(255)             NOT NULL,
    email           VARCHAR(255)             NOT NULL,
    name            VARCHAR(100)             NOT NULL,
    profile_image   VARCHAR(500),
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_hide         BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN users.user_id IS '구글 계정 이메일';
COMMENT ON COLUMN users.is_hide IS '논리 삭제 여부';

-- boards
CREATE TABLE boards (
    board_id        VARCHAR(255)             NOT NULL,
    user_id         VARCHAR(255)             NOT NULL,
    parent_board_id VARCHAR(255),
    board_name      VARCHAR(20)              NOT NULL,
    sort_order      INTEGER                  NOT NULL DEFAULT 0,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_hide         BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_boards PRIMARY KEY (board_id),
    CONSTRAINT fk_boards_user   FOREIGN KEY (user_id)         REFERENCES users (user_id),
    CONSTRAINT fk_boards_parent FOREIGN KEY (parent_board_id) REFERENCES boards (board_id)
);

CREATE TRIGGER trg_boards_updated_at
    BEFORE UPDATE ON boards
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN boards.board_id IS '보드 ID (클라이언트 생성)';
COMMENT ON COLUMN boards.parent_board_id IS '상위 보드 (NULL = 최상위)';
COMMENT ON COLUMN boards.board_name IS '보드명 최대 20자';

-- memo_types
CREATE TABLE memo_types (
    type_id       VARCHAR(50)  NOT NULL,
    type_name     VARCHAR(50)  NOT NULL,
    default_color VARCHAR(20)  NOT NULL,
    shape_css     VARCHAR(255) NOT NULL,
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    CONSTRAINT pk_memo_types PRIMARY KEY (type_id)
);

COMMENT ON COLUMN memo_types.default_color IS 'HEX 색상코드';

-- memos
CREATE TABLE memos (
    memo_id    VARCHAR(255)             NOT NULL,
    board_id   VARCHAR(255)             NOT NULL,
    type_id    VARCHAR(50)              NOT NULL,
    content    TEXT,
    pos_x      REAL                     NOT NULL DEFAULT 0,
    pos_y      REAL                     NOT NULL DEFAULT 0,
    width      REAL                     NOT NULL DEFAULT 200,
    height     REAL                     NOT NULL DEFAULT 200,
    z_index    INTEGER                  NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    is_hide    BOOLEAN                  NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_memos      PRIMARY KEY (memo_id),
    CONSTRAINT fk_memos_board FOREIGN KEY (board_id) REFERENCES boards     (board_id),
    CONSTRAINT fk_memos_type  FOREIGN KEY (type_id)  REFERENCES memo_types (type_id)
);

CREATE TRIGGER trg_memos_updated_at
    BEFORE UPDATE ON memos
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

COMMENT ON COLUMN memos.memo_id IS '메모 ID (클라이언트 생성)';
COMMENT ON COLUMN memos.content IS 'Rich Text 내용';
COMMENT ON COLUMN memos.pos_x IS 'X 좌표';
COMMENT ON COLUMN memos.pos_y IS 'Y 좌표';
COMMENT ON COLUMN memos.z_index IS '노출 순서';

-- 기본 메모 타입 마스터 데이터
INSERT INTO memo_types (type_id, type_name, default_color, shape_css, sort_order)
VALUES
    ('TYPE_BASIC',   '기본 메모',   '#FEF08A', 'memo-basic',   1),
    ('TYPE_ROUND',   '둥근 메모',   '#BBF7D0', 'memo-round',   2),
    ('TYPE_SHADOW',  '그림자 메모', '#BFDBFE', 'memo-shadow',  3),
    ('TYPE_MINIMAL', '미니멀 메모', '#FED7AA', 'memo-minimal', 4),
    ('TYPE_DARK',    '다크 메모',   '#374151', 'memo-dark',    5);
```

---

## 🌐 REST API 설계

### 공통 규칙

| 항목 | 규칙 |
| --- | --- |
| Base URL | `/api/v1` |
| 인증 방식 | Spring Session (Cookie: `SESSION`) — JWT 미사용 |
| Content-Type | `application/json` |
| 성공 Response | `{ "success": true, "data": object|null, "message": string, "timestamp": string }` |
| 에러 Response | `{ "success": false, "errorCode": string, "message": string, "timestamp": string }` |
| HTTP 상태 코드 | 200 OK / 201 Created / 400 Bad Request / 401 Unauthorized / 404 Not Found / 500 Internal Server Error |
| 논리 삭제 | DELETE 메서드 사용 — 내부적으로 `is_hide = TRUE` 처리 |
| 자동 저장 | Frontend 1초 딜레이 후 호출. Backend 동일 API 멱등성 보장 |

### 인증 API

| Method | Endpoint | 설명 | 인증 필요 | 관련 REQ |
| --- | --- | --- | --- | --- |
| POST | `/api/v1/auth/login` | 구글 OAuth 토큰으로 로그인. 신규 사용자 자동 생성. 세션 발급 | N | AUTH-001~007 |
| POST | `/api/v1/auth/logout` | 세션 만료 처리 | Y | AUTH-009~011 |
| GET | `/api/v1/auth/me` | 현재 세션 사용자 정보 조회 (세션 유효 검증) | Y | AUTH-007 |

**POST /api/v1/auth/login — Request/Response 예시**

```json
// Request
{
  "googleToken": "ya29.a0AfH6SMC..."
}

// Response (성공)
{
  "success": true,
  "data": {
    "userId": "dhlee@gmail.com",
    "name": "이동훈",
    "profileImage": "https://..."
  },
  "message": "로그인 성공",
  "timestamp": "2025-02-25T10:00:00"
}
```

### 보드 API

| Method | Endpoint | 설명 | 인증 필요 | 관련 REQ |
| --- | --- | --- | --- | --- |
| GET | `/api/v1/boards` | 로그인 사용자의 전체 보드 트리 조회 (is_hide=FALSE) | Y | BOARD-001 |
| POST | `/api/v1/boards` | 새 보드 생성 (ID는 클라이언트가 생성하여 전달) | Y | BOARD-004,005,007 |
| PATCH | `/api/v1/boards/{boardId}/name` | 보드 이름 수정 (최대 20자 서버 검증) | Y | BOARD-009~011 |
| PATCH | `/api/v1/boards/{boardId}/move` | 보드 위치(부모 보드, 순서) 변경 | Y | BOARD-015 |
| DELETE | `/api/v1/boards/{boardId}` | 보드 논리 삭제 (is_hide = TRUE) | Y | BOARD-012~014 |

**POST /api/v1/boards — Request/Response 예시**

```json
// Request
{
  "boardId": "dhlee@gmail.com_10@9eikf",
  "parentBoardId": null,
  "boardName": "새 보드",
  "sortOrder": 1
}

// Response (성공)
{
  "success": true,
  "data": {
    "boardId": "dhlee@gmail.com_10@9eikf",
    "boardName": "새 보드",
    "parentBoardId": null,
    "sortOrder": 1
  },
  "message": "보드가 생성되었습니다.",
  "timestamp": "2025-02-25T10:00:00"
}
```

### 메모 API

| Method | Endpoint | 설명 | 인증 필요 | 관련 REQ |
| --- | --- | --- | --- | --- |
| GET | `/api/v1/boards/{boardId}/memos` | 보드 내 전체 메모 조회 (is_hide=FALSE, z_index 순 정렬) | Y | MEMO-001~005 |
| POST | `/api/v1/boards/{boardId}/memos` | 새 메모 생성 (ID, 타입, 초기 위치 포함) | Y | MEMO-006~011 |
| PATCH | `/api/v1/memos/{memoId}/content` | 메모 내용(Rich Text) 수정 | Y | MEMO-012~013 |
| PATCH | `/api/v1/memos/{memoId}/position` | 메모 위치(pos_x, pos_y) 수정 | Y | MEMO-014 |
| PATCH | `/api/v1/memos/{memoId}/size` | 메모 크기(width, height) 수정 | Y | MEMO-015 |
| PATCH | `/api/v1/boards/{boardId}/memos/zindex` | 보드 내 메모 노출 순서(z_index) 일괄 수정 | Y | MEMO-016~018 |
| DELETE | `/api/v1/memos/{memoId}` | 메모 논리 삭제 (is_hide = TRUE) | Y | MEMO-019~021 |

**PATCH /api/v1/boards/{boardId}/memos/zindex — Request 예시**

```json
// Request (노출 순서 일괄 수정)
{
  "memos": [
    { "memoId": "dhlee@gmail.com_10@9eikf_a!di2$3", "zIndex": 3 },
    { "memoId": "dhlee@gmail.com_10@9eikf_b!kf9@2", "zIndex": 1 },
    { "memoId": "dhlee@gmail.com_10@9eikf_c#lm5!1", "zIndex": 2 }
  ]
}
```

### 메모 타입 API

| Method | Endpoint | 설명 | 인증 필요 | 관련 REQ |
| --- | --- | --- | --- | --- |
| GET | `/api/v1/memo-types` | 사용 가능한 전체 메모 타입 목록 조회 (is_active=TRUE) | Y | MEMO-024 |

---

## 🔒 보안 설계

### 인증/인가 정책

| 항목 | 정책 | 관련 REQ |
| --- | --- | --- |
| 인증 방식 | Google OAuth 2.0 — 구글 계정 선택 후 서버 세션 발급 | AUTH-001 |
| JWT 사용 | **미사용 (SRS 제약 조건) — 서버 세션만 사용** | NFR-005 |
| 세션 저장소 | Spring Session (JDBC 기반) | AUTH-006 |
| 세션 만료 | 24시간 (86400초) TTL. 만료 시 401 응답 → 클라이언트 로그인 이동 | AUTH-008 |
| API 접근 제어 | 모든 `/api/**` 엔드포인트는 유효 세션 필수. `/api/v1/auth/login` 만 예외 | NFR-006 |
| CORS 설정 | Frontend Origin (`localhost:5173`) 만 허용. `credentials: true` (쿠키 허용) | NFR-006 |
| HttpOnly Cookie | `SESSION` 쿠키 `HttpOnly=true`, `SameSite=Lax` 설정으로 XSS 방어 | NFR-005 |
| 데이터 접근 제어 | 보드/메모 API는 로그인 사용자 본인의 데이터만 조회/수정 가능 | NFR-006 |

### SecurityConfig 설계 방향

```java
// SecurityConfig.java (설계 방향)
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
            )
            .logout(logout -> logout
                .logoutUrl("/api/v1/auth/logout")
                .invalidateHttpSession(true)
                .deleteCookies("SESSION")
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
        // ...
    }
}
```

---

## 🛠 환경 설정

### Backend — application.yml

```yaml
spring:
  profiles:
    active: local

  jpa:
    database: postgresql
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: validate   # 운영: validate / 개발 초기: create
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        default_batch_fetch_size: 100
        default_schema: memo_board_V3

  session:
    store-type: jdbc
    timeout: 86400s   # 24시간

server:
  port: 8080
  servlet:
    context-path: /
```

### Backend — application-local.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://aws-1-ap-south-1.pooler.supabase.com:6543/memo_board_V3?sslmode=require
    username: postgres.wcvyfovaiigrkmzsqrpq
    password: "!@!L1e2e34!!!"
    driver-class-name: org.postgresql.Driver
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
      connection-timeout: 30000
      connection-init-sql: SET search_path TO memo_board_v3

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: <GOOGLE_CLIENT_ID>
            client-secret: <GOOGLE_CLIENT_SECRET>
            scope: email, profile
            redirect-uri: http://localhost:8080/login/oauth2/code/google

logging:
  level:
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql: TRACE
```

### Frontend — .env.local

```
VITE_API_BASE_URL=http://localhost:8080/api/v1
VITE_GOOGLE_CLIENT_ID=<GOOGLE_CLIENT_ID>
VITE_AUTO_SAVE_DELAY=1000
```

### Frontend — vite.config.ts

```tsx
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import path from 'path';

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
```

### Git Repository 정보

| 구분 | Repository URL | 브랜치 전략 |
| --- | --- | --- |
| Frontend | https://github.com/sfood-it-dev-ax/SFOOD-MB-WEB-V1.git | `main` (배포) / `develop` (개발) / `feature/*` (기능) |
| Backend | https://github.com/sfood-it-dev-ax/SFOOD-MB-APP-V1.git | `main` (배포) / `develop` (개발) / `feature/*` (기능) |

---

## 🚀 개발 우선순위 및 구현 순서

> SRS 우선순위 (🔴 High → 🟡 Medium → 🟢 Low) 기준으로 아래 순서로 구현을 권장한다.
>

| 단계 | 구현 항목 | SRS REQ | 비고 |
| --- | --- | --- | --- |
| Step 1 | DB 스키마 생성 (users, boards, memo_types, memos) | DATA-001~003 | Backend 선행 |
| Step 2 | Spring Boot 프로젝트 초기 설정 (Gradle, JPA, Security, CORS) |  | Backend |
| Step 3 | 구글 OAuth 로그인 / 세션 발급 / 로그아웃 API | AUTH-001~011 | Backend |
| Step 4 | Vite React 프로젝트 초기 설정, 라우팅, Axios 설정 |  | Frontend |
| Step 5 | 로그인 화면 및 구글 OAuth 클라이언트 연동 | AUTH-001~008 | Frontend |
| Step 6 | 보드 CRUD API (Controller → Service → Repository) | BOARD-001~015 | Backend |
| Step 7 | LNB 보드 트리 UI + 보드 CRUD 연동 | BOARD-001~015 | Frontend |
| Step 8 | 메모 CRUD API (내용/위치/크기/순서/삭제) | MEMO-001~024 | Backend |
| Step 9 | 메모 카드 컴포넌트 + 에디터 툴바 + 메모 툴바 | MEMO-001~024 | Frontend |
| Step 10 | 자동 저장 훅 (1초 딜레이 + 실패 롤백) | AUTO-001~004 | Frontend |
| Step 11 | 보드 드래그 이동 (LNB 트리 계층 변경) | BOARD-015 | Frontend |
| Step 12 | 보드 줌 (Ctrl + 마우스 휠 Zoom In/Out) | UI-001 | Frontend |
| Step 13 | 통합 테스트 및 성능 최적화 | NFR 전체 | 공통 |

---

## 📝 문서 변경 이력

| 버전 | 일자 | 변경 내용 | 변경자 | 승인자 |
| --- | --- | --- | --- | --- |
| v1.0 | 2025.02.25 | SRS-MEMO-2025-001 기반 최초 작성 |  |  |
| v1.1 | 2025.02.26 | DB RDBMS 변경: MySQL 8.0 (Local) → PostgreSQL (Supabase) 반영. DDL, application.yml, build.gradle 전체 수정 |  |  |

---

> 본 문서는 SRS-MEMO-2025-001 v1.0 기반으로 작성된 아키텍처 설계안입니다.
>