package com.example.note;

import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ReadNoteService {

    List<NoteResponseBody> read();

    NoteResponseBody read(@NotNull UUID id);
}
