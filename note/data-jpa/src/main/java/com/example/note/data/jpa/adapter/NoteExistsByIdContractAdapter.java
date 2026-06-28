package com.example.note.data.jpa.adapter;

import java.util.UUID;

import com.example.note.contract.NoteExistsByIdContract;
import com.example.note.data.jpa.repository.NoteJpaRepository;

import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdContractAdapter implements NoteExistsByIdContract {

    private final NoteJpaRepository noteJpaRepository;

    NoteExistsByIdContractAdapter(NoteJpaRepository noteJpaRepository) {
        this.noteJpaRepository = noteJpaRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteJpaRepository.existsById(id);
    }

}
