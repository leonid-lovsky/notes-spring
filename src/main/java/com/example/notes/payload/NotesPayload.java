package com.example.notes.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NotesPayload(
    @NotNull String content
) {

}
