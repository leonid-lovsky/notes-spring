package com.example.note.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteServiceInterfaceReactive;
import com.example.note.data.mongodb.reactive.mapper.NoteMongoReactiveMapperContract;
import com.example.note.data.mongodb.reactive.model.NoteReactiveDocument;
import com.example.note.data.mongodb.reactive.repository.NoteMongoReactiveRepository;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteService implements NoteServiceInterfaceReactive {

    private final NoteMongoReactiveRepository noteMongoReactiveRepository;

    private final NoteMongoReactiveMapperContract noteMongoReactiveMapper;

    NoteService(NoteMongoReactiveRepository noteMongoReactiveRepository,
            NoteMongoReactiveMapperContract noteMongoReactiveMapper) {
        this.noteMongoReactiveRepository = noteMongoReactiveRepository;
        this.noteMongoReactiveMapper = noteMongoReactiveMapper;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.noteMongoReactiveRepository.existsById(id);
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        NoteReactiveDocument document = this.noteMongoReactiveMapper.toNewDocument(request);
        return this.noteMongoReactiveRepository.insert(document).map(this.noteMongoReactiveMapper::toResponse);
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteMongoReactiveRepository.findAll().map(this.noteMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<NoteResponse> findById(UUID id) {
        return this.noteMongoReactiveRepository.findById(id)
            .map(this.noteMongoReactiveMapper::toResponse)
            .switchIfEmpty(Mono.error(new NoteNotFoundException(id)));
    }

    @Override
    public Mono<NoteResponse> replace(UUID id, NoteRequest request) {
        return this.noteMongoReactiveRepository.existsById(id).flatMap((exists) -> {
            if (!exists) {
                return Mono.error(new NoteNotFoundException(id));
            }
            NoteReactiveDocument document = this.noteMongoReactiveMapper.toExistingDocument(id, request);
            return this.noteMongoReactiveRepository.save(document).map(this.noteMongoReactiveMapper::toResponse);
        });
    }

    @Override
    public Mono<NoteResponse> merge(UUID id, NoteRequest request) {
        return findById(id).flatMap((existing) -> {
            NoteRequest merged = merge(existing, request);
            NoteReactiveDocument document = this.noteMongoReactiveMapper.toExistingDocument(id, merged);
            return this.noteMongoReactiveRepository.save(document).map(this.noteMongoReactiveMapper::toResponse);
        });
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.noteMongoReactiveRepository.existsById(id)
            .flatMap((exists) -> exists ? this.noteMongoReactiveRepository.deleteById(id)
                    : Mono.error(new NoteNotFoundException(id)));
    }

    private static NoteRequest merge(NoteResponse existing, NoteRequest request) {
        String content = (request.content() != null) ? request.content() : existing.content();
        return new NoteRequest(content);
    }

}
