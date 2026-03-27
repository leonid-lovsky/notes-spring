package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteServiceDelete {

    NotePayloadResponse delete(@NotNull UUID id);
}
