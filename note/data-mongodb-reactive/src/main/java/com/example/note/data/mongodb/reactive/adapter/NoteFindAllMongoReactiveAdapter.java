package com.example.note.data.mongodb.reactive.adapter;

import com.example.note.contract.reactive.NoteFindAllContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteReactiveDocumentMapperContract;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindAllMongoReactiveAdapter implements NoteFindAllContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteReactiveDocumentMapperContract noteReactiveDocumentMapper;

    NoteFindAllMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteReactiveDocumentMapperContract noteReactiveDocumentMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteReactiveDocumentMapper = noteReactiveDocumentMapper;
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteMongoReactiveRepository.findAll().map(this.noteReactiveDocumentMapper::toResponse);
    }

}
