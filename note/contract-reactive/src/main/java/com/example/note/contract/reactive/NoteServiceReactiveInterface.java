package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteServiceReactiveInterface extends NoteReactiveInterface {

    @Override
    Mono<Boolean> existsById(UUID id);

    @Override
    Mono<NoteResponse> add(NoteRequest request);

    @Override
    Flux<NoteResponse> findAll();

    @Override
    Mono<NoteResponse> findById(UUID id);

    @Override
    Mono<NoteResponse> replace(UUID id, NoteRequest request);

    @Override
    Mono<NoteResponse> merge(UUID id, NoteRequest request);

    @Override
    Mono<Void> remove(UUID id);
}
