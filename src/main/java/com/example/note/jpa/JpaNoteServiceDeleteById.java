package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceDeleteById;
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
class JpaNoteServiceDeleteById implements NoteServiceDeleteById {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse deleteById(@NotNull UUID id) {
        JpaNoteEntity jpaNoteEntity = jpaNoteRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
        jpaNoteRepository.delete(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }
}
