package com.example.note;

import java.util.UUID;

public interface DeleteNoteByIdSynchronous {

    NoteResponse execute(UUID id);
}
