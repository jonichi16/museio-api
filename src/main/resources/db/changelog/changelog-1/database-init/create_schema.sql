-- Enable pgcrypto for UUID generation
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ref_table table
CREATE TABLE ref_table (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(125) UNIQUE NOT NULL,
    label VARCHAR(255),
    type VARCHAR(125)
);

-- account table
CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(125) UNIQUE NOT NULL,
    name VARCHAR(125) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ
);

CREATE INDEX idx_account_email ON account (email);

-- profile table
CREATE TABLE profile (
    account_id BIGINT PRIMARY KEY,
    username VARCHAR(125) UNIQUE NOT NULL,
    bio TEXT,
    profile_picture VARCHAR(512),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_profile_account FOREIGN KEY (account_id)
          REFERENCES account (id)
          ON DELETE CASCADE
);

-- collection table
CREATE TABLE collection (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    portfolio VARCHAR(125) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_collection_account FOREIGN KEY (account_id)
          REFERENCES account (id)
          ON DELETE CASCADE
);

CREATE INDEX idx_collection_account_id ON collection (account_id);

-- art table
CREATE TABLE art (
    id BIGSERIAL PRIMARY KEY,
    collection_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(512) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_art_collection FOREIGN KEY (collection_id)
        REFERENCES collection (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_art_collection_id ON art (collection_id);

-- tag table
CREATE TABLE tag (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(125) UNIQUE NOT NULL
);

-- art_tag junction table (many-to-many)
CREATE TABLE art_tag (
    id BIGSERIAL PRIMARY KEY,
    art_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,

    CONSTRAINT fk_art_tag_art FOREIGN KEY (art_id)
        REFERENCES art (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_art_tag_tag FOREIGN KEY (tag_id)
        REFERENCES tag (id)
        ON DELETE CASCADE,

    CONSTRAINT uq_art_tag UNIQUE (art_id, tag_id)
);

CREATE INDEX idx_art_tag_art_id ON art_tag (art_id);
CREATE INDEX idx_art_tag_tag_id ON art_tag (tag_id);
