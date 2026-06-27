package com.example.note.data.jpa;

import com.example.note.domain.NoteReplacePort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
class NoteReplacePortAdapter implements NoteReplacePort {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapper noteJpaMapper;

    NoteReplacePortAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapper noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public NoteResponse replace(UUID id, NoteRequest request) {
        NoteEntity saved = noteJpaRepository.save(noteJpaMapper.toExistingEntity(id, request));
        return noteJpaMapper.toResponse(saved);
    }

}
