CREATE TABLE IF NOT EXISTS user_notes (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    user_id UUID NOT NULL,
    note_id UUID NOT NULL,
    role VARCHAR NOT NULL,
    CONSTRAINT uq_user_notes_user_id_note_id UNIQUE (user_id, note_id)
);
