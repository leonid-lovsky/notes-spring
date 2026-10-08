package com.example.usernote;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserNoteRequest(
    @NotNull UUID userId,
    @NotNull UUID noteId
) {

}
