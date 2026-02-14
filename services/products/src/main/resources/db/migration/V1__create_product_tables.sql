CREATE TABLE categories
(
    id   UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE products
(
    id                UUID PRIMARY KEY,
    merchant_id       UUID             NOT NULL,
    category_id       UUID             NOT NULL REFERENCES categories (id) ON DELETE RESTRICT,
    title             VARCHAR(255)     NOT NULL,
    description       TEXT,

    price             BIGINT           NOT NULL CHECK (price >= 0),

    quantity_in_stock INTEGER          NOT NULL CHECK (quantity_in_stock >= 0),

    average_rating    DOUBLE PRECISION NOT NULL DEFAULT 0.0 CHECK (average_rating >= 0.0 AND average_rating <= 5.0),
    review_count      INTEGER          NOT NULL DEFAULT 0 CHECK (review_count >= 0),

    is_active         BOOLEAN          NOT NULL DEFAULT TRUE,

    created_at        TIMESTAMP        NOT NULL,
    updated_at        TIMESTAMP        NOT NULL
);


CREATE TABLE reviews
(
    id          UUID PRIMARY KEY,
    product_id  UUID      NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    customer_id UUID      NOT NULL,

    rating      INTEGER   NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment     TEXT,

    created_at  TIMESTAMP NOT NULL
);


CREATE INDEX idx_products_merchant_id ON products (merchant_id);

CREATE INDEX idx_products_category_active ON products (category_id, is_active);

CREATE INDEX idx_reviews_product_id ON reviews (product_id);

CREATE UNIQUE INDEX uk_reviews_product_customer ON reviews (product_id, customer_id);