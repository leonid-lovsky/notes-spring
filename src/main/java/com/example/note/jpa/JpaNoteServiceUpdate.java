package com.example.note.jpa;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class JpaNoteServiceUpdate implements NoteServiceUpdate {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse update(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = jpaNoteRepository.findById(id)
            // TODO: repetitive logic findById, consider refactoring
            // TODO: exception handling, consider refactoring
            // TODO: internationalization, consider refactoring
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
        jpaNoteMapper.updateNoteJpaEntity(notePayloadRequest, jpaNoteEntity);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }

    @Override
    public NotePayloadResponse replace(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = jpaNoteRepository.findById(id)
            // TODO: repetitive logic findById, consider refactoring
            // TODO: exception handling, consider refactoring
            // TODO: internationalization, consider refactoring
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
        jpaNoteMapper.replaceNoteJpaEntity(notePayloadRequest, jpaNoteEntity);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }
}
