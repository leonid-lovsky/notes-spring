package com.example.note.application.service;

import com.example.note.CreateNoteService;
import com.example.note.RequestNotePayload;
import com.example.note.ResponseNotePayload;
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
    public ResponseNotePayload create(@Valid @NotNull RequestNotePayload requestNotePayload) {
        return null;
    }
}
