package com.example.notes.payload;

import jakarta.validation.constraints.NotBlank;

public record NotesRequest(
    @NotBlank String content
) {

}
