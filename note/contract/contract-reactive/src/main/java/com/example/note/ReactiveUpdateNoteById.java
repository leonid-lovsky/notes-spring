package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveUpdateNoteById {

    Mono<NoteResponse> updateNoteById(UUID id, NoteRequest request);
}
