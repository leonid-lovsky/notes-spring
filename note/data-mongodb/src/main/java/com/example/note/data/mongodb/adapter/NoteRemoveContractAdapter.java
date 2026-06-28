package com.example.note.data.mongodb.adapter;

import java.util.UUID;

import com.example.note.contract.NoteRemoveContract;
import com.example.note.data.mongodb.repository.NoteMongoRepository;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemoveContractAdapter implements NoteRemoveContract {

    private final NoteMongoRepository noteMongoRepository;

    NoteRemoveContractAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public void remove(UUID id) {
        this.noteMongoRepository.deleteById(id);
    }

}
