package com.example.note;

import java.util.UUID;

public interface DeleteNoteById {

    NoteResponse deleteNoteById(UUID id);
}
