package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteUpdateService {

    NoteResponseModel update(@NotNull UUID id, @Valid @NotNull NoteRequestModel noteRequestModel);
}
