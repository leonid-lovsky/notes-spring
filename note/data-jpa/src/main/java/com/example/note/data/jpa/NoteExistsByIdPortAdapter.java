package com.example.note.data.jpa;

import java.util.UUID;

import com.example.note.domain.NoteExistsByIdPort;

import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdPortAdapter implements NoteExistsByIdPort {

    private final NoteJpaRepository noteJpaRepository;

    NoteExistsByIdPortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteJpaRepository.existsById(id);
    }

}
