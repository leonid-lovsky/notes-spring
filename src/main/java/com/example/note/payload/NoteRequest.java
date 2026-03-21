package com.example.note.payload;

import jakarta.validation.constraints.NotBlank;

public record NoteRequest(
    @NotBlank
    String content
) {

}
