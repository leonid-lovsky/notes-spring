package com.example.note.data.mongodb.reactive.adapter;

import com.example.note.contract.reactive.NoteAddContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteMongoReactiveMapperContract;
import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteAddMongoReactiveAdapter implements NoteAddContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteMongoReactiveMapperContract noteMongoReactiveMapper;

    NoteAddMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteMongoReactiveMapperContract noteMongoReactiveMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteMongoReactiveMapper = noteMongoReactiveMapper;
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        NoteReactiveDocument document = this.noteMongoReactiveMapper.toNewDocument(request);
        return this.noteMongoReactiveRepository.insert(document).map(this.noteMongoReactiveMapper::toResponse);
    }

}
