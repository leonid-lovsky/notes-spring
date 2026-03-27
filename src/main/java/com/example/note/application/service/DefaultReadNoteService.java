package com.example.note.application.service;

import com.example.note.ReadNoteService;
import com.example.note.application.NoteRepository;
import com.example.note.presentation.rest.NoteResponseBody;
import jakarta.validation.constraints.NotNull;
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
class DefaultReadNoteService implements ReadNoteService {

    private final NoteRepository noteRepository;

    @Override
    public List<NoteResponseBody> read() {
        return List.of();
    }

    @Override
    public NoteResponseBody read(@NotNull UUID id) {
        return null;
    }
}
