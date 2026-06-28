package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteRemoveContractReactive;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteRemoveMongoReactiveAdapter implements NoteRemoveContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    NoteRemoveMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.noteMongoReactiveRepository.deleteById(id);
    }

}
