package com.example.notes.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record NotesResponse(
    @NotNull UUID id,
    @NotNull String content
) {

}
