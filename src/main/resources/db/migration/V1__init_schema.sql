-- =============================================================
-- V1 : 초기 스키마 생성
-- CREATE TABLE IF NOT EXISTS 로 기존 DB에서도 안전하게 실행됩니다.
-- =============================================================

CREATE TABLE IF NOT EXISTS users (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(255),
    password   VARCHAR(255),
    email      VARCHAR(255),
    role       VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS route (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255),
    owner_id   BIGINT REFERENCES users (id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS route_user (
    route_id BIGINT NOT NULL REFERENCES route (id),
    user_id  BIGINT NOT NULL REFERENCES users (id),
    PRIMARY KEY (route_id, user_id)
);

CREATE TABLE IF NOT EXISTS access (
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255),
    content    TEXT,
    user_id    BIGINT REFERENCES users (id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS image (
    id         BIGSERIAL PRIMARY KEY,
    image_url  VARCHAR(255),
    access_id  BIGINT REFERENCES access (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notification (
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT,
    message    VARCHAR(255),
    type       VARCHAR(255),
    is_read    BOOLEAN      NOT NULL DEFAULT FALSE,
    content    VARCHAR(255),
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS invite (
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255),
    route_id   BIGINT,
    expired_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS memo (
    id         BIGSERIAL PRIMARY KEY,
    content    VARCHAR(255),
    route_id   BIGINT REFERENCES route (id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS place (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    link        VARCHAR(255),
    address     VARCHAR(255),
    roadaddress VARCHAR(255),
    mapx        VARCHAR(255),
    mapy        VARCHAR(255),
    category    VARCHAR(255),
    route_id    BIGINT REFERENCES route (id),
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP
);

-- RouteItem : JOINED 전략 — 공통 컬럼을 부모 테이블에 보관
CREATE TABLE IF NOT EXISTS route_item (
    id         BIGSERIAL PRIMARY KEY,
    "order"    INTEGER,
    day        INTEGER,
    route_id   BIGINT REFERENCES route (id),
    dtype      VARCHAR(31),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

-- PlaceItem : route_item 을 PK/FK로 공유
CREATE TABLE IF NOT EXISTS place_item (
    id       BIGINT PRIMARY KEY REFERENCES route_item (id),
    place_id BIGINT REFERENCES place (id)
);

-- MemoItem : route_item 을 PK/FK로 공유
CREATE TABLE IF NOT EXISTS memo_item (
    id      BIGINT PRIMARY KEY REFERENCES route_item (id),
    memo_id BIGINT REFERENCES memo (id)
);
