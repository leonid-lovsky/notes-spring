package com.example.note;

import jakarta.validation.constraints.NotNull;

public record NotePayloadRequest(
    @NotNull String content
) {

}
