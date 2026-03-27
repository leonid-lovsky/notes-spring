package com.example.note.presentation.rest;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record NoteResponseBody(
    @NotNull UUID id,
    @NotNull String content
) {

}
