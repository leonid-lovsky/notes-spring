package com.example.note.web;

import jakarta.validation.constraints.NotBlank;

public record ReplaceNoteRequest(
    @NotBlank
    String content
) {

}
