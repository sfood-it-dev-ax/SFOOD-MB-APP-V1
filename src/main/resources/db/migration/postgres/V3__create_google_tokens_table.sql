CREATE SCHEMA IF NOT EXISTS memo_board;
SET search_path TO memo_board, public;

CREATE TABLE IF NOT EXISTS mb_google_tokens (
    user_id VARCHAR(255) PRIMARY KEY,
    google_token TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_mb_google_tokens_user FOREIGN KEY (user_id) REFERENCES mb_users(user_id)
);
