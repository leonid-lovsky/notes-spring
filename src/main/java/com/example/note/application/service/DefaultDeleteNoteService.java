package com.example.note.application.service;

import com.example.note.DeleteNoteService;
import com.example.note.ResponseNotePayload;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class DefaultDeleteNoteService implements DeleteNoteService {

    private final NoteRepository noteRepository;

    @Override
    public ResponseNotePayload delete(@NotNull UUID id) {
        return null;
    }
}
