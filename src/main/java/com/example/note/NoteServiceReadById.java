package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteServiceReadById {

    NotePayloadResponse readById(@NotNull UUID id);
}
