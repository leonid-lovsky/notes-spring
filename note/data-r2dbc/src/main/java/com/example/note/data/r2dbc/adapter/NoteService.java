package com.example.note.data.r2dbc.adapter;

import java.util.UUID;

import com.example.note.contract.reactive.NoteServiceReactiveInterface;
import com.example.note.data.r2dbc.mapper.NoteR2dbcMapperContract;
import com.example.note.data.r2dbc.repository.NoteR2dbcRepository;
import com.example.note.domain.NoteNotFoundException;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class NoteService implements NoteServiceReactiveInterface {

    private final NoteR2dbcRepository noteR2dbcRepository;

    private final NoteR2dbcMapperContract noteR2dbcMapper;

    NoteService(NoteR2dbcRepository noteR2dbcRepository, NoteR2dbcMapperContract noteR2dbcMapper) {
        this.noteR2dbcRepository = noteR2dbcRepository;
        this.noteR2dbcMapper = noteR2dbcMapper;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.noteR2dbcRepository.existsById(id);
    }

    @Override
    public Mono<NoteResponse> add(NoteRequest request) {
        return this.noteR2dbcRepository.save(this.noteR2dbcMapper.toNewEntity(request))
            .map(this.noteR2dbcMapper::toResponse);
    }

    @Override
    public Flux<NoteResponse> findAll() {
        return this.noteR2dbcRepository.findAll().map(this.noteR2dbcMapper::toResponse);
    }

    @Override
    public Mono<NoteResponse> findById(UUID id) {
        return this.noteR2dbcRepository.findById(id)
            .map(this.noteR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new NoteNotFoundException(id)));
    }

    @Override
    public Mono<NoteResponse> replace(UUID id, NoteRequest request) {
        return this.noteR2dbcRepository.existsById(id)
            .flatMap((exists) -> exists
                    ? this.noteR2dbcRepository.save(this.noteR2dbcMapper.toExistingEntity(id, request))
                        .map(this.noteR2dbcMapper::toResponse)
                    : Mono.error(new NoteNotFoundException(id)));
    }

    @Override
    public Mono<NoteResponse> merge(UUID id, NoteRequest request) {
        return findById(id).flatMap((existing) -> {
            NoteRequest merged = merge(existing, request);
            return this.noteR2dbcRepository.save(this.noteR2dbcMapper.toExistingEntity(id, merged))
                .map(this.noteR2dbcMapper::toResponse);
        });
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.noteR2dbcRepository.existsById(id)
            .flatMap((exists) -> exists ? this.noteR2dbcRepository.deleteById(id)
                    : Mono.error(new NoteNotFoundException(id)));
    }

    private static NoteRequest merge(NoteResponse existing, NoteRequest request) {
        String content = (request.content() != null) ? request.content() : existing.content();
        return new NoteRequest(content);
    }
}
