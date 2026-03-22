package com.example.note.service;

import com.example.note.NoteCreateService;
import com.example.note.NoteRequestModel;
import com.example.note.NoteResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class NoteJpaCreateService implements NoteCreateService {

    @Override
    public NoteResponseModel create(NoteRequestModel requestBody) {
        return null;
    }
}
