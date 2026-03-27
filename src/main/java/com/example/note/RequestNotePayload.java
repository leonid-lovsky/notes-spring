package com.example.note;

import jakarta.validation.constraints.NotNull;

public record RequestNotePayload(
    @NotNull String content
) {

}
