package com.example.note.data.jpa;

import java.util.UUID;

import com.example.note.domain.NoteRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemovePortAdapter implements NoteRemovePort {

    private final NoteJpaRepository noteJpaRepository;

    NoteRemovePortAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public void remove(UUID id) {
        this.noteJpaRepository.deleteById(id);
    }

}
