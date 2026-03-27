package com.example.note;

import com.example.note.presentation.rest.NoteResponseModel;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteDeleteService {

    NoteResponseModel delete(@NotNull UUID id);
}
