package com.example.note.data.jpa;

import com.example.note.domain.NoteFindByIdPort;
import com.example.note.domain.NoteResponse;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

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
        return noteJpaRepository.findById(id).map(noteJpaMapper::toResponse);
    }
}
