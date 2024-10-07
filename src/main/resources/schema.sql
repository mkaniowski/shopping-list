CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "users"
(
    "id"          UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "email"       VARCHAR(256)                   NOT NULL,
    "username"    VARCHAR(64)                    NOT NULL,
    "password"    VARCHAR(256)                   NOT NULL,
    "roles"       VARCHAR(24)[]                  NOT NULL DEFAULT ARRAY ['ROLE_USER'],
    "is_verified" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "is_disabled" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "is_deleted"  TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_at"  TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_at"  TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    UNIQUE ("email"),
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "lists"
(
    "id"         UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "user_id"    UUID                           NOT NULL,
    "name"       VARCHAR(256)                   NOT NULL,
    "products"   UUID[]                         NULL     DEFAULT ARRAY []::uuid[],
    "is_deleted" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "products"
(
    "id"         UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "name"       VARCHAR(256)                   NOT NULL,
    "is_deleted" TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "items"
(
    "id"            UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "list_id"       UUID                           NOT NULL,
    "item_id"       UUID                           NOT NULL,
    "quantity"      FLOAT                          NOT NULL,
    "quantity_type" VARCHAR(32)                    NOT NULL,
    "is_deleted"    TIMESTAMP(0) WITHOUT TIME ZONE NULL,
    "updated_at"    TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "created_at"    TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY ("id")
)
