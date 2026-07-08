package com.example.note.contract.reactive;

import java.util.UUID;

import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NoteContractReactive {

    Mono<NoteResponse> add(NoteRequest request);

    Mono<Boolean> existsById(UUID id);

    Flux<NoteResponse> findAll();

    Mono<NoteResponse> findById(UUID id);

    Mono<Void> remove(UUID id);

    Mono<NoteResponse> replace(UUID id, NoteRequest request);

}
