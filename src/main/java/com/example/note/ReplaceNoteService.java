package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface ReplaceNoteService {

    ResponseNotePayload replace(@NotNull UUID id, @Valid @NotNull RequestNotePayload requestNotePayload);
}
