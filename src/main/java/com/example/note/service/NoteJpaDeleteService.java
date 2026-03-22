package com.example.note.service;

import com.example.note.NoteDeleteService;
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
class NoteJpaDeleteService implements NoteDeleteService {

    @Override
    public NoteResponseModel delete(UUID id) {
        return null;
    }
}
