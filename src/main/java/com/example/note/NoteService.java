package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface NoteService {

    NotePayloadResponse create(@Valid @NotNull NotePayloadRequest notePayloadRequest);

    List<NotePayloadResponse> read();

    NotePayloadResponse readById(@NotNull UUID id);

    NotePayloadResponse updateById(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest);

    NotePayloadResponse replaceById(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest);

    NotePayloadResponse deleteById(@NotNull UUID id);
}
