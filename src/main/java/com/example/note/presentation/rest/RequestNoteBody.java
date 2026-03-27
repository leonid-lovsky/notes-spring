package com.example.note.presentation.rest;

import jakarta.validation.constraints.NotNull;

record RequestNoteBody(
    @NotNull String content
) {

}
