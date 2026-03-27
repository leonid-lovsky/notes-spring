package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface ReadNoteService {

    List<ResponseNotePayload> read();

    ResponseNotePayload read(@NotNull UUID id);
}
