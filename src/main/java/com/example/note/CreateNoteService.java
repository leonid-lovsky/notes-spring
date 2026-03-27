package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateNoteService {

    ResponseNotePayload create(@Valid @NotNull RequestNotePayload requestNotePayload);
}
