package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceRead;
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
class JpaNoteServiceRead implements NoteServiceRead {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public List<NotePayloadResponse> read() {
        return List.of();
    }

    @Override
    public NotePayloadResponse read(@NotNull UUID id) {
        return null;
    }
}
