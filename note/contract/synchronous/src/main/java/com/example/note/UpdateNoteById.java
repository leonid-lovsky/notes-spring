package com.example.note;

import java.util.UUID;

public interface UpdateNoteById {

    NoteResponse updateNoteById(UUID id, NoteRequest request);
}
