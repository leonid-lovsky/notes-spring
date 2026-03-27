package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface NoteServiceCreate {

    NotePayloadResponse create(@Valid @NotNull NotePayloadRequest notePayloadRequest);
}
