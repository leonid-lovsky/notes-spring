package com.example.usernote.contract;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserNoteRequest(
    @NotNull UUID userId,
    @NotNull UUID noteId
) {
}
