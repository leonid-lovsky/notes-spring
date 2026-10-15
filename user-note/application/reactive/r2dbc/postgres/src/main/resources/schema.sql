CREATE TABLE IF NOT EXISTS "user_note" (
    "id" UUID PRIMARY KEY,
    "user_id" UUID NOT NULL,
    "note_id" UUID NOT NULL,
    "role" VARCHAR(16) NOT NULL
);
