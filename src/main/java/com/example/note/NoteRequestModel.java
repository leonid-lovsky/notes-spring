package com.example.note;

import jakarta.validation.constraints.NotNull;

public record NoteRequestModel(
    @NotNull String content
) {

}
