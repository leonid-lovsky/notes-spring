package com.example.note;

import jakarta.validation.constraints.NotBlank;

record NoteRequest(
    @NotBlank String content
) {

}
