package com.example.usernote.data.mongodb;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

class UserNoteKey implements Serializable {

    private UUID userId;
    private UUID noteId;

    protected UserNoteKey() {
    }

    UserNoteKey(UUID userId, UUID noteId) {
        this.userId = userId;
        this.noteId = noteId;
    }

    UUID getUserId() {
        return userId;
    }

    UUID getNoteId() {
        return noteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserNoteKey that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, noteId);
    }
}
