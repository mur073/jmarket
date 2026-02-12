CREATE TABLE users
(
    id                UUID PRIMARY KEY,
    name              VARCHAR(255)        NOT NULL,
    email             VARCHAR(255) UNIQUE NOT NULL,
    is_email_verified BOOLEAN DEFAULT FALSE,
    created_at        TIMESTAMP           NOT NULL,
    updated_at        TIMESTAMP           NOT NULL
);

CREATE INDEX idx_users_email ON users (email);