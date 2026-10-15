package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUpdateNoteById {

    Mono<NoteResponse> updateNoteById(UUID id, NoteRequest request);
}
