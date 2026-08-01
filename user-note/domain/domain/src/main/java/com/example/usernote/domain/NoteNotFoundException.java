package com.example.usernote.domain;

import java.util.UUID;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(UUID noteId) {
        super("Note not found: " + noteId);
    }
}
