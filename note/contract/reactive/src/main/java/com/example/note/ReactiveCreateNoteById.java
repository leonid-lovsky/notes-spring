package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveCreateNoteById {

    Mono<NoteResponse> createNoteById(UUID id, NoteRequest request);
}
