package com.example.note.application.service;

import com.example.note.NoteDeleteService;
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
class DefaultNoteDeleteService implements NoteDeleteService {

    @Override
    public NoteResponse delete(UUID id) {
        return null;
    }
}
