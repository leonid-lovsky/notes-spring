package com.example.notes.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record NotesInput(
    @NotNull String content
) {

}
