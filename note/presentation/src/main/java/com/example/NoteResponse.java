package com.example;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

record NoteResponse(
    @NotNull UUID id,
    @NotNull String content
) {

}
