package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveCreateNoteById {

    Mono<NoteResponse> createNoteById(UUID id, NoteRequest request);
}
