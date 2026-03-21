package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    NoteResponseBody create(@Valid @NotNull NoteRequestBody requestBody);

    List<NoteResponseBody> read();

    NoteResponseBody read(@NotNull UUID id);

    NoteResponseBody update(@NotNull UUID id, @Valid @NotNull NoteRequestBody requestBody);

    NoteResponseBody replace(@NotNull UUID id, @Valid @NotNull NoteRequestBody requestBody);

    NoteResponseBody delete(@NotNull UUID id);
}
