package com.example.usernote.domain;

import java.util.UUID;

public record UserNote(UUID userId, UUID noteId, UserNoteRole role) {

    public UserNote withRole(UserNoteRole role) {
        return new UserNote(this.userId, this.noteId, role);
    }
}
