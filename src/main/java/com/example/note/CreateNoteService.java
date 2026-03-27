package com.example.note;

import com.example.note.presentation.rest.NoteRequestBody;
import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateNoteService {

    NoteResponseBody create(@Valid @NotNull NoteRequestBody noteRequestBody);
}
