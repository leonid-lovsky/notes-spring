package com.example.note;

import java.util.UUID;

public interface FindNoteByIdSynchronous {

    NoteResponse execute(UUID id);
}
