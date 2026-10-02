package com.example.note;

import java.util.UUID;

public interface FindNoteById {

    NoteResponse findNoteById(UUID id);
}
