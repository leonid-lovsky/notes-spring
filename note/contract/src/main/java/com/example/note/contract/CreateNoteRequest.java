package com.example.note.contract;

import jakarta.validation.constraints.NotBlank;

public record CreateNoteRequest(
    @NotBlank String content
) {
}
