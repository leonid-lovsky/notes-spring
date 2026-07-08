package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteContractReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteMongoReactiveMapperContract;
import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteMongoReactiveAdapter implements NoteContractReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteMongoReactiveMapperContract noteMongoReactiveMapper;

    NoteMongoReactiveAdapter(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteMongoReactiveMapperContract noteMongoReactiveMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteMongoReactiveMapper = noteMongoReactiveMapper;
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        NoteReactiveDocument document = this.noteMongoReactiveMapper.toNewDocument(request);
        return this.noteMongoReactiveRepository.insert(document).map(this.noteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.noteMongoReactiveRepository.existsById(id);
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteMongoReactiveRepository.findAll().map(this.noteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<NoteResponse> findById(UUID id) {
        return this.noteMongoReactiveRepository.findById(id).map(this.noteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.noteMongoReactiveRepository.deleteById(id);
    }

    @Override
    public Mono<NoteResponse> replace(UUID id, NoteRequest request) {
        NoteReactiveDocument document = this.noteMongoReactiveMapper.toExistingDocument(id, request);
        return this.noteMongoReactiveRepository.save(document).map(this.noteMongoReactiveMapper::toResponse);
    }

}
