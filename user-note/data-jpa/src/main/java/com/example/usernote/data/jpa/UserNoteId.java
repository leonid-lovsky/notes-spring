package com.example.usernote.data.jpa;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
class UserNoteId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;

    private UUID noteId;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteId() {

    }

    UserNoteId(UUID userId, UUID noteId) {
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
        if (!(o instanceof UserNoteId that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, noteId);
    }
}
