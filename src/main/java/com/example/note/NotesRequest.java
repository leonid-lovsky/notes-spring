package com.example.note;

import jakarta.validation.constraints.NotBlank;

record NotesRequest(
    @NotBlank String content
) {

}
