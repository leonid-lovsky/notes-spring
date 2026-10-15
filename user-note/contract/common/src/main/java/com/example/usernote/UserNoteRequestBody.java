package com.example.usernote;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserNoteRequestBody(
    @NotNull UUID userId,
    @NotNull UUID noteId,
    @NotNull UserNoteRole role
) {

}
