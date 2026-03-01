package com.example.notes.notes;

import jakarta.validation.constraints.NotBlank;

public record NotesRequest(
    @NotBlank String content
) {

}
