package com.example.note.application.service;

import com.example.note.CreateNoteService;
import com.example.note.application.NoteRepository;
import com.example.note.presentation.rest.NoteRequestBody;
import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class DefaultCreateNoteService implements CreateNoteService {

    private final NoteRepository noteRepository;

    @Override
    public NoteResponseBody create(@Valid @NotNull NoteRequestBody noteRequestBody) {
        return null;
    }
}
