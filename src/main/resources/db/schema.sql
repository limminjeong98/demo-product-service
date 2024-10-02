CREATE TABLE brands
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    brand_name VARCHAR(100)          NOT NULL,
    CONSTRAINT pk_brands PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    category_type VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE products
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    category_id BIGINT                NULL,
    brand_id    BIGINT                NULL,
    price       BIGINT                NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);