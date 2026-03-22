package com.example.note.service;

import com.example.note.NoteReplaceService;
import com.example.note.NoteRequestModel;
import com.example.note.NoteResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class NoteJpaReplaceService implements NoteReplaceService {

    @Override
    public NoteResponseModel replace(UUID id, NoteRequestModel noteRequestModel) {
        return null;
    }
}
