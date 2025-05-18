-- Enable pgcrypto for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(125),
    name VARCHAR(125),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
