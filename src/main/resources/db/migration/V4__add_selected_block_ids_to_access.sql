CREATE TABLE access_selected_blocks (
    access_id BIGINT NOT NULL REFERENCES access(id) ON DELETE CASCADE,
    block_id  BIGINT NOT NULL
);
