-- V2 : refresh_token 테이블 추가 및 users 테이블에 uuid 컬럼 추가

CREATE TABLE IF NOT EXISTS refresh_token (
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(512) NOT NULL UNIQUE,
    username   VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_refresh_token_username ON refresh_token(username);

ALTER TABLE users ADD COLUMN IF NOT EXISTS uuid VARCHAR(36);
UPDATE users SET uuid = gen_random_uuid()::TEXT WHERE uuid IS NULL;
ALTER TABLE users ALTER COLUMN uuid SET NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS idx_users_uuid ON users(uuid);
