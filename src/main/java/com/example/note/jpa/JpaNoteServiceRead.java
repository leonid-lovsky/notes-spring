package com.example.note.jpa;

import com.example.note.NotePayloadResponse;
import com.example.note.NoteServiceRead;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

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
}
