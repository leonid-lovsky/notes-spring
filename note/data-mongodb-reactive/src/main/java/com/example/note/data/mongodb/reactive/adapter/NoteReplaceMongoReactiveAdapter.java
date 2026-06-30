package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteReplaceContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteMongoReactiveMapperContract;
import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteReplaceMongoReactiveAdapter implements NoteReplaceContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteMongoReactiveMapperContract noteMongoReactiveMapper;

    NoteReplaceMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteMongoReactiveMapperContract noteMongoReactiveMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteMongoReactiveMapper = noteMongoReactiveMapper;
    }

    @Override
    public Mono<NoteResponse> replace(UUID id, NoteRequest request) {
        NoteReactiveDocument document = this.noteMongoReactiveMapper.toExistingDocument(id, request);
        return this.noteMongoReactiveRepository.save(document).map(this.noteMongoReactiveMapper::toResponse);
    }

}
