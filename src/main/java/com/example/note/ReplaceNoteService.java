package com.example.note;

import com.example.note.presentation.rest.NoteRequestBody;
import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ReplaceNoteService {

    NoteResponseBody replace(@NotNull UUID id, @Valid @NotNull NoteRequestBody noteRequestBody);
}
