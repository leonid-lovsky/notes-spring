package com.example.note.data.jpa.adapter;

import java.util.UUID;

import com.example.note.contract.NoteRemoveContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemoveContractAdapter implements NoteRemoveContract {

    private final NoteJpaRepository noteJpaRepository;

    NoteRemoveContractAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public void remove(UUID id) {
        this.noteJpaRepository.deleteById(id);
    }

}
