package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public interface NoteReadService {

    List<NoteResponseModel> read();

    NoteResponseModel read(@NotNull UUID id);
}
