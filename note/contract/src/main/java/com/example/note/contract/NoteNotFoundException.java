package com.example.note.contract;

import com.example.crud.contract.ResourceNotFoundException;

import java.util.UUID;

public class NoteNotFoundException extends ResourceNotFoundException {

    public NoteNotFoundException(UUID noteId) {
        super("Note", noteId);
    }
}
