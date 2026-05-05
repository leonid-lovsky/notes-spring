package com.example.note.contract;

import jakarta.validation.constraints.NotBlank;

public record UpdateNoteRequest(
    @NotBlank String content
) {
}
