package com.example.usernote.contract;

import com.example.crud.contract.ResourceNotFoundException;

import java.util.UUID;

public class UserNoteNotFoundException extends ResourceNotFoundException {

    public UserNoteNotFoundException(UUID userNoteId) {
        super("UserNote", userNoteId);
    }
}
