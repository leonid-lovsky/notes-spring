package com.example.note;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveFindNoteById {

    Mono<NoteResponse> findNoteById(UUID id);
}
