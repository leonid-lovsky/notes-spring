package com.example.note.data.mongodb;

import java.util.UUID;

import com.example.note.domain.NoteExistsByIdPort;

import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdPortAdapter implements NoteExistsByIdPort {

    private final NoteMongoRepository noteMongoRepository;

    NoteExistsByIdPortAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public boolean existsById(UUID id) {
        return this.noteMongoRepository.existsById(id);
    }

}
