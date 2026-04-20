package com.example;

import jakarta.validation.constraints.NotNull;

record NoteRequest(
    @NotNull String content
) {

}
