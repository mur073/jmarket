CREATE TABLE customer_profiles
(
    id         UUID PRIMARY KEY,
    user_id    UUID      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE merchant_profiles
(
    id                  UUID PRIMARY KEY,
    user_id             UUID             NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    shop_name           VARCHAR(255)     NOT NULL,
    inn                 VARCHAR(50)      NOT NULL,
    legal_address       TEXT             NOT NULL,
    verification_status VARCHAR(50)      NOT NULL,
    rating              DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    created_at          TIMESTAMP        NOT NULL,
    updated_at          TIMESTAMP        NOT NULL
);

CREATE TABLE customer_addresses
(
    id           UUID PRIMARY KEY,
    customer_id  UUID         NOT NULL REFERENCES customer_profiles (id) ON DELETE CASCADE,
    region       VARCHAR(255) NOT NULL,
    city         VARCHAR(255) NOT NULL,
    street       VARCHAR(255),
    house_number VARCHAR(50),
    zip_code     VARCHAR(20)  NOT NULL,
    is_default   BOOLEAN      NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_merchant_verification ON merchant_profiles (verification_status);