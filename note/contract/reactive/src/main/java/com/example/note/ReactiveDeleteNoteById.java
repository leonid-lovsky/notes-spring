package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveDeleteNoteById {

    Mono<NoteResponse> deleteNoteById(UUID id);
}
