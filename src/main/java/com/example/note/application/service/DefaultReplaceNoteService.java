package com.example.note.application.service;

import com.example.note.ReplaceNoteService;
import com.example.note.RequestNotePayload;
import com.example.note.ResponseNotePayload;
import jakarta.validation.Valid;
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
class DefaultReplaceNoteService implements ReplaceNoteService {

    private final NoteRepository noteRepository;

    @Override
    public ResponseNotePayload replace(@NotNull UUID id, @Valid @NotNull RequestNotePayload requestNotePayload) {
        return null;
    }
}
