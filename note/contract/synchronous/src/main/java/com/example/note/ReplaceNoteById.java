package com.example.note;

import java.util.UUID;

public interface ReplaceNoteById {

    NoteResponse replaceNoteById(UUID id, NoteRequest request);
}
