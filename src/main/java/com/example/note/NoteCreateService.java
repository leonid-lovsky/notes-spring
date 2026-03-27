package com.example.note;

import com.example.note.presentation.rest.NoteRequestModel;
import com.example.note.presentation.rest.NoteResponseModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface NoteCreateService {

    NoteResponseModel create(@Valid @NotNull NoteRequestModel noteRequestModel);
}
