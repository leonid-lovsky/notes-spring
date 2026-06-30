package com.example.note.data.mongodb.reactive.adapter;

import com.example.note.contract.reactive.NoteAddContractReactive;
import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.mapper.NoteReactiveDocumentMapperContract;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteAddMongoReactiveAdapter implements NoteAddContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteReactiveDocumentMapperContract noteReactiveDocumentMapper;

    NoteAddMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteReactiveDocumentMapperContract noteReactiveDocumentMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteReactiveDocumentMapper = noteReactiveDocumentMapper;
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        NoteReactiveDocument document = this.noteReactiveDocumentMapper.toNewDocument(request);
        return this.noteMongoReactiveRepository.insert(document).map(this.noteReactiveDocumentMapper::toResponse);
    }

}
