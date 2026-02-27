CREATE SCHEMA IF NOT EXISTS memo_board;
SET search_path TO memo_board, public;

CREATE TABLE IF NOT EXISTS mb_users (
    user_id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    profile_image VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_hide BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS mb_boards (
    board_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    parent_board_id VARCHAR(255),
    board_name VARCHAR(20) NOT NULL,
    sort_order INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_hide BOOLEAN NOT NULL,
    CONSTRAINT fk_mb_boards_user FOREIGN KEY (user_id) REFERENCES mb_users(user_id),
    CONSTRAINT fk_mb_boards_parent FOREIGN KEY (parent_board_id) REFERENCES mb_boards(board_id)
);

CREATE TABLE IF NOT EXISTS mb_memo_types (
    type_id VARCHAR(50) PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    default_color VARCHAR(20) NOT NULL,
    shape_css VARCHAR(255) NOT NULL,
    sort_order INTEGER NOT NULL,
    is_active BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS mb_memos (
    memo_id VARCHAR(255) PRIMARY KEY,
    board_id VARCHAR(255) NOT NULL,
    type_id VARCHAR(50) NOT NULL,
    content TEXT,
    pos_x REAL NOT NULL,
    pos_y REAL NOT NULL,
    width REAL NOT NULL,
    height REAL NOT NULL,
    z_index INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    is_hide BOOLEAN NOT NULL,
    CONSTRAINT fk_mb_memos_board FOREIGN KEY (board_id) REFERENCES mb_boards(board_id),
    CONSTRAINT fk_mb_memos_type FOREIGN KEY (type_id) REFERENCES mb_memo_types(type_id)
);

INSERT INTO mb_memo_types(type_id, type_name, default_color, shape_css, sort_order, is_active)
VALUES
    ('TYPE_BASIC', '기본 메모', '#FEF08A', 'memo-basic', 1, TRUE),
    ('TYPE_ROUND', '둥근 메모', '#BBF7D0', 'memo-round', 2, TRUE),
    ('TYPE_SHADOW', '그림자 메모', '#BFDBFE', 'memo-shadow', 3, TRUE)
ON CONFLICT (type_id) DO NOTHING;
