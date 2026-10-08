package com.example.usernote;

import java.util.UUID;

public class UserNoteAlreadyExistsException extends RuntimeException {

    public UserNoteAlreadyExistsException(UUID id) {
        super("UserNote already exists: " + id);
    }
}
