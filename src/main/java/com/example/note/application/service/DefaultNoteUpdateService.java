package com.example.note.application.service;

import com.example.note.NoteUpdateService;
import com.example.note.presentation.rest.NoteRequest;
import com.example.note.presentation.rest.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class DefaultNoteUpdateService implements NoteUpdateService {

    @Override
    public NoteResponse update(UUID id, NoteRequest noteRequestModel) {
        return null;
    }
}
