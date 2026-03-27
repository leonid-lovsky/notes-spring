package com.example.note.jpa;

import com.example.note.NotePayloadRequest;
import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceCreate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
class JpaNoteServiceCreate implements NoteServiceCreate {

    private final JpaNoteRepository jpaNoteRepository;
    private final JpaNoteMapper jpaNoteMapper;

    @Override
    public NotePayloadResponse create(@Valid @NotNull NotePayloadRequest notePayloadRequest) {
        JpaNoteEntity jpaNoteEntity = jpaNoteMapper.toJpaNoteEntity(notePayloadRequest);
        jpaNoteRepository.save(jpaNoteEntity);
        return jpaNoteMapper.toNotePayloadResponse(jpaNoteEntity);
    }
}
