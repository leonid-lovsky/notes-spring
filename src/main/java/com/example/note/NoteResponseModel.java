package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NoteResponseModel(
    @NotNull UUID id,
    @NotNull String content
) {

}
