CREATE TABLE categories (
    id VARCHAR(64) PRIMARY KEY,
    label VARCHAR(128) NOT NULL,
    tagline VARCHAR(512) NOT NULL DEFAULT ''
);

CREATE TABLE products (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id VARCHAR(64) NOT NULL REFERENCES categories (id)
);

CREATE TABLE product_highlights (
    product_id VARCHAR(64) NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    sort_order INT NOT NULL,
    highlight TEXT NOT NULL,
    PRIMARY KEY (product_id, sort_order)
);

CREATE TABLE variants (
    sku VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL REFERENCES products (id) ON DELETE CASCADE,
    label VARCHAR(128) NOT NULL,
    color_hex VARCHAR(16) NOT NULL,
    price NUMERIC(12, 2) NOT NULL
);

CREATE TABLE stores (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(128) NOT NULL,
    address_line VARCHAR(255) NOT NULL
);

CREATE TABLE inventory (
    sku VARCHAR(64) PRIMARY KEY REFERENCES variants (sku) ON DELETE CASCADE,
    available_units INT NOT NULL CHECK (available_units >= 0)
);

CREATE INDEX idx_products_category ON products (category_id);
CREATE INDEX idx_variants_product ON variants (product_id);
