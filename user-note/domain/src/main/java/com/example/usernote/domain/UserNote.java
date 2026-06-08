package com.example.usernote.domain;

import java.util.UUID;

public final class UserNote {

    private final UUID userId;
    private final UUID noteId;
    private UserNoteRole role;

    public UserNote(UUID userId, UUID noteId, UserNoteRole role) {
        this.userId = userId;
        this.noteId = noteId;
        this.role = role;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getNoteId() {
        return noteId;
    }

    public UserNoteRole getRole() {
        return role;
    }

    public void setRole(UserNoteRole role) {
        this.role = role;
    }
}
