package com.example.note.data.jpa;

import java.util.UUID;

import com.example.note.domain.NoteReplacePort;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

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
        NoteEntity saved = this.noteJpaRepository.save(this.noteJpaMapper.toExistingEntity(id, request));
        return this.noteJpaMapper.toResponse(saved);
    }

}
