package com.example.note;

import java.util.UUID;

public interface CreateNoteById {

    NoteResponse createNoteById(UUID id, NoteRequest request);
}
