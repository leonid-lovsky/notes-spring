package com.example.application.notes;

import jakarta.validation.constraints.NotBlank;

record NotesRequest(
    @NotBlank String content
) {

}

