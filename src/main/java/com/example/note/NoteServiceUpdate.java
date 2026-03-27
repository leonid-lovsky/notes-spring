package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteServiceUpdate {

    NotePayloadResponse update(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest);

    NotePayloadResponse replace(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest);
}
