package com.example.note;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface NoteDeleteService {

    NoteResponseModel delete(@NotNull UUID id);
}
