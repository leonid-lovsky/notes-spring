package com.example.note;

import jakarta.validation.constraints.NotBlank;

public record NoteRequestBody(
    @NotBlank
    String content
) {

}
