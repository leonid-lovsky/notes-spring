package com.example.usernote;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserNoteResponseBody(
    @NotNull UUID id,
    @NotNull UUID userId,
    @NotNull UUID noteId,
    @NotNull UserNoteRole role
) {

}
