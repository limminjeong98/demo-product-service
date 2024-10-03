CREATE TABLE brands
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    brand_name VARCHAR(100)          NOT NULL,
    CONSTRAINT pk_brands PRIMARY KEY (id)
);

CREATE INDEX idx_brands_name ON brands (id, brand_name);

CREATE TABLE categories
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    category_type VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE INDEX idx_categories_type ON categories (id, category_type);

CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT                NULL,
    brand_id    BIGINT                NULL,
    price       BIGINT                NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE INDEX idx_products_detail ON products (id, category_id, brand_id, price);