package com.example.usernote;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserNote(
    @NotNull UUID id,
    @NotNull UUID userId,
    @NotNull UUID noteId
) {

}
