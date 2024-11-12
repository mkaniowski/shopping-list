CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "users"
(
    "id"          UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "email"       VARCHAR(256)                   NOT NULL,
    "username"    VARCHAR(64)                    NOT NULL,
    "roles"       VARCHAR(24)[]                  NOT NULL DEFAULT ARRAY ['ROLE_USER'],
    "is_verified" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "is_disabled" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "is_deleted"  TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_at"  TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_at"  TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    UNIQUE ("email"),
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "products"
(
    "id"       UUID         NOT NULL DEFAULT gen_random_uuid(),
    "name"     VARCHAR(256) NOT NULL,
    "category" VARCHAR(128) NULL,
    UNIQUE ("name"),
    PRIMARY KEY ("id")
);


CREATE TABLE IF NOT EXISTS "shopping_lists"
(
    "id"         UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "user_id"    UUID                           NOT NULL,
    "name"       VARCHAR(128)                   NOT NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS "shopping_list_items"
(
    "id"               UUID        NOT NULL DEFAULT gen_random_uuid(),
    "shopping_list_id" UUID        NOT NULL,
    "product_id"       UUID        NOT NULL,
    "quantity_type"    VARCHAR(32) NOT NULL,
    "quantity"         INT         NOT NULL,
    "notes"            TEXT        NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("shopping_list_id") REFERENCES "shopping_lists" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("product_id") REFERENCES "products" ("id") ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS "user_roles"
(
    user_id UUID NOT NULL,
    roles VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_product_name ON "products" (LOWER("name"));

