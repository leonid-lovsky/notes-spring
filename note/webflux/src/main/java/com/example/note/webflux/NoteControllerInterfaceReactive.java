package com.example.note.webflux;

import java.util.UUID;

import com.example.note.contract.reactive.NoteInterfaceReactive;
import com.example.note.domain.NoteRequest;
import com.example.note.domain.NoteResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

public interface NoteControllerInterfaceReactive extends NoteInterfaceReactive {

    @Override
    Mono<ResponseEntity<Boolean>> existsById(UUID id);

    @Override
    Mono<ResponseEntity<NoteResponse>> add(NoteRequest request);

    @Override
    ResponseEntity<Flux<NoteResponse>> findAll();

    @Override
    Mono<ResponseEntity<NoteResponse>> findById(UUID id);

    @Override
    Mono<ResponseEntity<NoteResponse>> replace(UUID id, NoteRequest request);

    @Override
    Mono<ResponseEntity<NoteResponse>> merge(UUID id, NoteRequest request);

    @Override
    Mono<ResponseEntity<Void>> remove(UUID id);

}
