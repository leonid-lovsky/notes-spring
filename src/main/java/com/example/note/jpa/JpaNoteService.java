package com.example.note.jpa;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteService;
import jakarta.validation.Valid;
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
class JpaNoteService implements NoteService {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    @Transactional
    public NotePayloadResponse create(@Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = jpaNoteMapper.toJpaNoteEntity(notePayloadRequest);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }

    @Override
    public List<NotePayloadResponse> read() {
        return jpaNoteRepository.findAll().stream()
            .map(jpaNoteMapper::toNotePayloadResponse)
            .toList();
    }

    @Override
    public NotePayloadResponse readById(@NotNull UUID id) {
        return jpaNoteMapper.toNotePayloadResponse(findById(id));
    }

    @Override
    @Transactional
    public NotePayloadResponse updateById(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = findById(id);
        jpaNoteMapper.updateNoteJpaEntity(notePayloadRequest, jpaNoteEntity);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }

    @Override
    @Transactional
    public NotePayloadResponse replaceById(@NotNull UUID id, @Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = findById(id);
        jpaNoteMapper.replaceNoteJpaEntity(notePayloadRequest, jpaNoteEntity);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }

    @Override
    @Transactional
    public NotePayloadResponse deleteById(@NotNull UUID id) {
        JpaNoteEntity jpaNoteEntity = findById(id);
        jpaNoteRepository.delete(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }

    private JpaNoteEntity findById(UUID id) {
        return jpaNoteRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Note not found: " + id));
    }
}
