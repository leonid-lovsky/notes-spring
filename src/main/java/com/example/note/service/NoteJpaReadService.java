package com.example.note.service;

import com.example.note.NoteReadService;
import com.example.note.NoteResponseModel;
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
class NoteJpaReadService implements NoteReadService {

    @Override
    public List<NoteResponseModel> read() {
        return List.of();
    }

    @Override
    public NoteResponseModel read(UUID id) {
        return null;
    }
}
