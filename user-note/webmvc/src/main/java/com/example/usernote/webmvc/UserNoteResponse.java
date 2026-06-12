package com.example.usernote.webmvc;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRole;

import java.util.UUID;

record UserNoteResponse(UUID userId, UUID noteId, UserNoteRole role) {

    static UserNoteResponse from(UserNote userNote) {
        return new UserNoteResponse(userNote.userId(), userNote.noteId(), userNote.role());
    }
}
