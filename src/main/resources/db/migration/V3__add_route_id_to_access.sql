-- V3 : access 테이블에 route_id 컬럼 추가

ALTER TABLE access ADD COLUMN IF NOT EXISTS route_id BIGINT REFERENCES route(id);
