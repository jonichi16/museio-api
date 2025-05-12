-- Enable pgcrypto for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE account (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(125),
    name VARCHAR(125),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);
