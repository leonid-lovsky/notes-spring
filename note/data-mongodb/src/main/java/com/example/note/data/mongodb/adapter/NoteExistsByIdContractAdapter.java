package com.example.note.data.mongodb.adapter;

import java.util.UUID;

import com.example.note.contract.NoteExistsByIdContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdContractAdapter implements NoteExistsByIdContract {

    private final NoteMongoRepository noteMongoRepository;

    NoteExistsByIdContractAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteMongoRepository.existsById(id);
    }

}
