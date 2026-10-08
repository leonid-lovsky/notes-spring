package com.example.usernote;

import java.util.UUID;

public class UserNoteNotFoundException extends RuntimeException {

    public UserNoteNotFoundException(UUID id) {
        super("UserNote not found: " + id);
    }
}
