package com.example.note;

import jakarta.validation.constraints.NotNull;

public record NoteRequestBody(
    @NotNull String content
) {

}
