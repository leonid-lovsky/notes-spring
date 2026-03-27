package com.example.note.presentation.rest;

import jakarta.validation.constraints.NotNull;

record NoteRequestBody(
    @NotNull String content
) {

}
