package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceRead;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
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
        return jpaNoteRepository.findAll().stream()
            .map(jpaNoteMapper::toNotePayloadResponse)
            .toList();
    }

    @Override
    public NotePayloadResponse read(@NotNull UUID id) {
        JpaNoteEntity jpaNoteEntity = jpaNoteRepository.findById(id)
            // TODO: repetitive logic findById, consider refactoring
            // TODO: exception handling, consider refactoring
            // TODO: internationalization, consider refactoring
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }
}
