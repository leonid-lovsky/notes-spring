package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceReadById;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
class JpaNoteServiceReadById implements NoteServiceReadById {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse readById(@NotNull UUID id) {
        JpaNoteEntity jpaNoteEntity = jpaNoteRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }
}
