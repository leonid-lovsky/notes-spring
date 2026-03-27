package com.example.note.application.service;

import com.example.note.NoteReadService;
import com.example.note.presentation.rest.NoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
class DefaultNoteReadService implements NoteReadService {

    @Override
    public List<NoteResponse> read() {
        return List.of();
    }

    @Override
    public NoteResponse read(UUID id) {
        return null;
    }
}
