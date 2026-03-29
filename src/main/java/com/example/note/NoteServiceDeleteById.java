package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteServiceDeleteById {

    NotePayloadResponse deleteById(@NotNull UUID id);
}
