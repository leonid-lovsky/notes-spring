package com.example.note;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record NoteResponse(
    @NotNull UUID id,
    @NotBlank String content
) {

}
