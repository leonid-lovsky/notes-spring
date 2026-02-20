package com.example.notes.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NotesOutput(
    @NotNull UUID id,
    @NotNull String content
) {

}
