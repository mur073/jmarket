CREATE TABLE carts
(
    id          UUID PRIMARY KEY,
    customer_id UUID      NOT NULL UNIQUE,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL
);

CREATE TABLE cart_items
(
    id          UUID PRIMARY KEY,
    cart_id     UUID      NOT NULL REFERENCES carts (id) ON DELETE CASCADE,
    product_id  UUID      NOT NULL,
    merchant_id UUID      NOT NULL,
    quantity    INTEGER   NOT NULL CHECK (quantity > 0),
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP NOT NULL,

    CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id)
);

CREATE TABLE orders
(
    id                  UUID PRIMARY KEY,
    customer_id         UUID        NOT NULL,
    shipping_address_id UUID        NOT NULL,
    status              VARCHAR(50) NOT NULL,
    total_amount        BIGINT      NOT NULL CHECK (total_amount >= 0),
    created_at          TIMESTAMP   NOT NULL,
    updated_at          TIMESTAMP   NOT NULL
);

CREATE TABLE order_items
(
    id          UUID PRIMARY KEY,
    order_id    UUID    NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    product_id  UUID    NOT NULL,
    merchant_id UUID    NOT NULL,
    quantity    INTEGER NOT NULL CHECK (quantity > 0),
    price       BIGINT  NOT NULL CHECK (price >= 0)
);

CREATE TABLE order_status_history
(
    id         UUID PRIMARY KEY,
    order_id   UUID        NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    status     VARCHAR(50) NOT NULL,
    created_at TIMESTAMP   NOT NULL
);

CREATE INDEX idx_carts_customer_id ON carts (customer_id);
CREATE INDEX idx_orders_customer_id ON orders (customer_id);
CREATE INDEX idx_order_items_merchant_id ON order_items (merchant_id);