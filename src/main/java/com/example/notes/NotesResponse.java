package com.example.notes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record NotesResponse(
    @NotNull UUID id,
    @NotBlank String content
) {

}
