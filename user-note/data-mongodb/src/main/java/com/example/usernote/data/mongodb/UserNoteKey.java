package com.example.usernote.data.mongodb;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

class UserNoteKey implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;

    private UUID noteId;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteKey() {

    }

    UserNoteKey(UUID userId, UUID noteId) {
        this.userId = userId;
        this.noteId = noteId;
    }

    UUID getUserId() {
        return this.userId;
    }

    UUID getNoteId() {
        return this.noteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserNoteKey that)) {
            return false;
        }
        return Objects.equals(this.userId, that.userId) && Objects.equals(this.noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.noteId);
    }

}
