package com.example.notes;

import jakarta.validation.constraints.NotBlank;

record NotesRequest(
    @NotBlank String content
) {

}