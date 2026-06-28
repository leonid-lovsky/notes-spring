package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteReplaceContractReactive;
import com.example.note.data.mongodb.reactive.document.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.mapper.NoteReactiveDocumentMapperContract;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceMongoReactiveAdapter implements NoteReplaceContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteReactiveDocumentMapperContract noteReactiveDocumentMapper;

    NoteReplaceMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteReactiveDocumentMapperContract noteReactiveDocumentMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteReactiveDocumentMapper = noteReactiveDocumentMapper;
    }

    @Override
    public Mono<NoteResponse> replace(UUID id, NoteRequest request) {
        NoteReactiveDocument document = this.noteReactiveDocumentMapper.toExistingDocument(id, request);
        return this.noteMongoReactiveRepository.save(document).map(this.noteReactiveDocumentMapper::toResponse);
    }

}
