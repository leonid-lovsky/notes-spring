package com.example.note;

import java.util.UUID;

public interface ReplaceNoteByIdSynchronous {

    NoteResponse execute(UUID id, NoteRequest request);
}
