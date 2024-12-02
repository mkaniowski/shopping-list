CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS "users"
(
    "id"          UUID                           NOT NULL,
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

CREATE TABLE IF NOT EXISTS "shopping_lists"
(
    "id"         UUID                           NOT NULL DEFAULT gen_random_uuid(),
    "user_id"    UUID                           NOT NULL,
    "name"       VARCHAR(128)                   NOT NULL,
    "products"   JSONB                          NULL,
    "created_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    "updated_at" TIMESTAMP(0) WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY ("id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS "user_roles"
(
    user_id UUID         NOT NULL,
    roles   VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_users_email ON "users" ("email");
CREATE INDEX IF NOT EXISTS idx_shopping_lists_user_id ON "shopping_lists" ("user_id");
CREATE INDEX IF NOT EXISTS idx_shopping_list_items_shopping_list_id ON "shopping_list_items" ("shopping_list_id");

