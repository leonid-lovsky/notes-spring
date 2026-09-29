package com.example.note;

import java.util.UUID;

public interface CreateNoteByIdSynchronous {

    NoteResponse execute(UUID id, NoteRequest request);
}
