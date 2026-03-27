package com.example.note.application.service;

import com.example.note.ReadNoteService;
import com.example.note.ResponseNotePayload;
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
    public List<ResponseNotePayload> read() {
        return List.of();
    }

    @Override
    public ResponseNotePayload read(@NotNull UUID id) {
        return null;
    }
}
