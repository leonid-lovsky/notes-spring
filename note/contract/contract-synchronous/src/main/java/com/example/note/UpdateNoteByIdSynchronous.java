package com.example.note;

import java.util.UUID;

public interface UpdateNoteByIdSynchronous {

    NoteResponse execute(UUID id, NoteRequest request);
}
