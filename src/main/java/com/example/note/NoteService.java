package com.example.note;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    NoteResponseBody create(@Valid NoteRequestBody requestBody);

    List<NoteResponseBody> read();

    NoteResponseBody read(UUID id);

    NoteResponseBody update(UUID id, @Valid NoteRequestBody requestBody);

    NoteResponseBody replace(UUID id, @Valid NoteRequestBody requestBody);

    NoteResponseBody delete(UUID id);
}
