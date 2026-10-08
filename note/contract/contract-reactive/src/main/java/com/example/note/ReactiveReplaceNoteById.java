package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveReplaceNoteById {

    Mono<NoteResponse> replaceNoteById(UUID id, NoteRequest request);
}
