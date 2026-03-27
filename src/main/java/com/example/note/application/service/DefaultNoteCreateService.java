package com.example.note.application.service;

import com.example.note.NoteCreateService;
import com.example.note.presentation.rest.NoteRequest;
import com.example.note.presentation.rest.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class DefaultNoteCreateService implements NoteCreateService {

    @Override
    public NoteResponse create(NoteRequest noteRequestModel) {
        return null;
    }
}
