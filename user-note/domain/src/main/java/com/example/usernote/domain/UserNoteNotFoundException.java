package com.example.usernote.domain;

import java.util.UUID;

public class UserNoteNotFoundException extends RuntimeException {

    public UserNoteNotFoundException(UUID userNoteId) {
        super("UserNote not found: " + userNoteId);
    }

    public UserNoteNotFoundException(UUID userId, UUID noteId) {
        super("UserNote not found: " + userId + "/" + noteId);
    }
}
