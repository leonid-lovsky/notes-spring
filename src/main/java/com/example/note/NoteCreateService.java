package com.example.note;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface NoteCreateService {

    NoteResponseModel create(@Valid @NotNull NoteRequestModel noteRequestModel);
}
