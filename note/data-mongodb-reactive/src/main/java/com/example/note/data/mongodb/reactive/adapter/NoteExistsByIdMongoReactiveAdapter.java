package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteExistsByIdContractReactive;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteExistsByIdMongoReactiveAdapter implements NoteExistsByIdContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    NoteExistsByIdMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.noteMongoReactiveRepository.existsById(id);
    }

}
