package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteFindByIdContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteReactiveDocumentMapperContract;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteFindByIdMongoReactiveAdapter implements NoteFindByIdContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteReactiveDocumentMapperContract noteReactiveDocumentMapper;

    NoteFindByIdMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteReactiveDocumentMapperContract noteReactiveDocumentMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteReactiveDocumentMapper = noteReactiveDocumentMapper;
    }

    @Override
    public Mono<NoteResponse> findById(UUID id) {
        return this.noteMongoReactiveRepository.findById(id).map(this.noteReactiveDocumentMapper::toResponse);
    }

}
