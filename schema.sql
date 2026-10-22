-- ===================================================================
-- Monolithic E-Commerce Application - Database Schema (Oracle 21c)
-- ===================================================================
-- NOTE: Hibernate auto-creates/updates this schema at runtime because
-- spring.jpa.hibernate.ddl-auto=update is set in application.properties.
-- This file documents the schema for reference, manual setup, and
-- portfolio/interview purposes (mirrors the actual JPA entity mappings).
--
-- Table/column names and NOT NULL constraints below match the entity
-- classes exactly (NOT NULL only where @JoinColumn(nullable=false) is
-- declared). Column lengths/precision are practical documentation
-- choices; DEFAULT values reflect the Java-level defaults set in the
-- entity classes (e.g. role = CUSTOMER, status = PENDING, active = true).
--
-- Run as: ecommerce_user (after granting required privileges)
-- Service: XEPDB1
-- ===================================================================

-- ===================================================================
-- 1. ADDRESSES  (Address.java -> @Entity(name = "addresses"))
-- ===================================================================
CREATE TABLE addresses (
    id       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    street   VARCHAR2(255),
    city     VARCHAR2(255),
    state    VARCHAR2(255),
    country  VARCHAR2(255),
    pincode  VARCHAR2(20)
);

-- ===================================================================
-- 2. USER_TABLE  (User.java -> @Entity(name = "user_table"))
-- ===================================================================
CREATE TABLE user_table (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name  VARCHAR2(100),
    last_name   VARCHAR2(100),
    email       VARCHAR2(150),
    phone       VARCHAR2(20),
    role        VARCHAR2(20) DEFAULT 'CUSTOMER',   -- stored as STRING (@Enumerated(EnumType.STRING))
    address_id  NUMBER,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_at   TIMESTAMP,                          -- field name in entity is "updateAt" (not "updatedAt")
    CONSTRAINT fk_user_address FOREIGN KEY (address_id) REFERENCES addresses(id)
);

-- ===================================================================
-- 3. PRODUCTS  (Product.java -> @Entity(name = "products"))
-- ===================================================================
CREATE TABLE products (
    id              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR2(255),
    description     VARCHAR2(1000),
    price           NUMBER(10,2),
    stock_quantity  NUMBER DEFAULT 0,
    category        VARCHAR2(100),
    image_url       VARCHAR2(500),
    active          NUMBER(1) DEFAULT 1,              -- Boolean -> NUMBER(1) (0/1) under Oracle dialect
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP
);

-- ===================================================================
-- 4. ORDERS  (Order.java -> @Entity(name = "orders"))
-- ===================================================================
CREATE TABLE orders (
    id            NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id       NUMBER NOT NULL,
    total_amount  NUMBER(10,2),
    status        VARCHAR2(20) DEFAULT 'PENDING',     -- @Enumerated(EnumType.STRING): PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELED
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES user_table(id)
);

-- ===================================================================
-- 5. ORDER_ITEM  (OrderItem.java -> default table name)
-- ===================================================================
CREATE TABLE order_item (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_id  NUMBER NOT NULL,
    quantity    NUMBER,
    price       NUMBER(10,2),
    order_id    NUMBER NOT NULL,
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_orderitem_order   FOREIGN KEY (order_id)   REFERENCES orders(id)
);

-- ===================================================================
-- 6. CART_ITEM  (CartItem.java -> default table name)
-- ===================================================================
CREATE TABLE cart_item (
    id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     NUMBER NOT NULL,
    product_id  NUMBER NOT NULL,
    quantity    NUMBER,
    price       NUMBER(10,2),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    CONSTRAINT fk_cartitem_user    FOREIGN KEY (user_id)    REFERENCES user_table(id),
    CONSTRAINT fk_cartitem_product FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ===================================================================
-- SAMPLE / SEED DATA
-- ===================================================================

-- Addresses
INSERT INTO addresses (street, city, state, country, pincode)
VALUES ('12 MG Road', 'Pune', 'Maharashtra', 'India', '411001');

INSERT INTO addresses (street, city, state, country, pincode)
VALUES ('45 Park Street', 'Mumbai', 'Maharashtra', 'India', '400001');

-- Users
INSERT INTO user_table (first_name, last_name, email, phone, role, address_id)
VALUES ('Shruti', 'Patil', 'shruti.patil@example.com', '9876543210', 'ADMIN', 1);

INSERT INTO user_table (first_name, last_name, email, phone, role, address_id)
VALUES ('Rahul', 'Sharma', 'rahul.sharma@example.com', '9876500000', 'CUSTOMER', 2);

-- Products
INSERT INTO products (name, description, price, stock_quantity, category, image_url, active)
VALUES ('Wireless Mouse', 'Ergonomic 2.4GHz wireless mouse', 799.00, 50, 'Electronics', 'https://example.com/mouse.jpg', 1);

INSERT INTO products (name, description, price, stock_quantity, category, image_url, active)
VALUES ('Mechanical Keyboard', 'RGB backlit mechanical keyboard', 2499.00, 30, 'Electronics', 'https://example.com/keyboard.jpg', 1);

INSERT INTO products (name, description, price, stock_quantity, category, image_url, active)
VALUES ('Laptop Stand', 'Adjustable aluminium laptop stand', 1299.00, 20, 'Accessories', 'https://example.com/stand.jpg', 1);

-- Cart item (Rahul adds a Wireless Mouse to cart)
INSERT INTO cart_item (user_id, product_id, quantity, price)
VALUES (2, 1, 1, 799.00);

-- Order (Rahul checks out — created via /api/orders, cart is cleared after)
INSERT INTO orders (user_id, total_amount, status)
VALUES (2, 799.00, 'CONFIRMED');

INSERT INTO order_item (product_id, quantity, price, order_id)
VALUES (1, 1, 799.00, 1);

COMMIT;
