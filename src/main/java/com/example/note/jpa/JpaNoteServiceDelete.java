package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceDelete;
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
class JpaNoteServiceDelete implements NoteServiceDelete {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse delete(@NotNull UUID id) {
        return null;
    }
}
