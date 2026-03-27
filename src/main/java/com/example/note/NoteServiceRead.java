package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface NoteServiceRead {

    List<NotePayloadResponse> read();

    NotePayloadResponse read(@NotNull UUID id);
}
