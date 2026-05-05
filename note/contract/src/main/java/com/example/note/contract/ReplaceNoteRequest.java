package com.example.note.contract;

import jakarta.validation.constraints.NotBlank;

public record ReplaceNoteRequest(
    @NotBlank String content
) {
}
