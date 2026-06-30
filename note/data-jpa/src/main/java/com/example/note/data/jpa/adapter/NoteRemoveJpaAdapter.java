package com.example.note.data.jpa.adapter;

import java.util.UUID;

import com.example.note.contract.NoteRemoveContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemoveJpaAdapter implements NoteRemoveContract {

    private final NoteJpaRepository noteJpaRepository;

    NoteRemoveJpaAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public void remove(UUID id) {
        this.noteJpaRepository.deleteById(id);
    }

}
