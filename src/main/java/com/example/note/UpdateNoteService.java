package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UpdateNoteService {

    ResponseNotePayload update(@NotNull UUID id, @Valid @NotNull RequestNotePayload requestNotePayload);
}
