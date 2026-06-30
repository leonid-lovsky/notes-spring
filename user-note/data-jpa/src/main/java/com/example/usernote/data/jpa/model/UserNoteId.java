package com.example.usernote.data.jpa.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserNoteId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;

    private UUID noteId;

    @SuppressWarnings("NullAway.Init")
    protected UserNoteId() {

    }

    public UserNoteId(UUID userId, UUID noteId) {
        this.userId = userId;
        this.noteId = noteId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public UUID getNoteId() {
        return this.noteId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserNoteId that)) {
            return false;
        }
        return Objects.equals(this.userId, that.userId) && Objects.equals(this.noteId, that.noteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userId, this.noteId);
    }

}
