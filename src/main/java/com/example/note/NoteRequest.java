package com.example.note;

import jakarta.validation.constraints.NotNull;

public record NoteRequest(
    @NotNull String content
) {

}
