package com.example.note.data.mongodb;

import java.util.UUID;

import com.example.note.domain.NoteRemovePort;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemovePortAdapter implements NoteRemovePort {

    private final NoteMongoRepository noteMongoRepository;

    NoteRemovePortAdapter(NoteMongoRepository noteMongoRepository) {
        this.noteMongoRepository = noteMongoRepository;
    }

    @Override
    public void remove(UUID id) {
        this.noteMongoRepository.deleteById(id);
    }

}
