package com.example.note.data.mongodb.reactive.adapter;

import com.example.note.contract.reactive.NoteFindAllContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteMongoReactiveMapperContract;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllMongoReactiveAdapter implements NoteFindAllContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteMongoReactiveMapperContract noteMongoReactiveMapper;

    NoteFindAllMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteMongoReactiveMapperContract noteMongoReactiveMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteMongoReactiveMapper = noteMongoReactiveMapper;
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteMongoReactiveRepository.findAll().map(this.noteMongoReactiveMapper::toResponse);
    }

}
