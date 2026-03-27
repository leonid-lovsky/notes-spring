package com.example.note;

import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface DeleteNoteService {

    NoteResponseBody delete(@NotNull UUID id);
}
