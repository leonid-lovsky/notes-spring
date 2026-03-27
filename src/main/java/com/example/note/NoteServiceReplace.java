package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteServiceReplace {

    NotePayloadResponse replace(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest);
}
