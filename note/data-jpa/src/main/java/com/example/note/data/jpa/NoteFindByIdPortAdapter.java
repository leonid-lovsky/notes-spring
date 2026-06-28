package com.example.note.data.jpa;

import java.util.Optional;
import java.util.UUID;

import com.example.note.domain.NoteFindByIdPort;
import com.example.note.domain.NoteResponse;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdPortAdapter implements NoteFindByIdPort {

    private final NoteJpaRepository noteJpaRepository;

    private final NoteJpaMapper noteJpaMapper;

    NoteFindByIdPortAdapter(NoteJpaRepository noteJpaRepository, NoteJpaMapper noteJpaMapper) {
        this.noteJpaRepository = noteJpaRepository;
        this.noteJpaMapper = noteJpaMapper;
    }

    @Override
    public Optional<NoteResponse> findById(UUID id) {
        return this.noteJpaRepository.findById(id).map(this.noteJpaMapper::toResponse);
    }

}
