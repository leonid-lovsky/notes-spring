package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateNoteByIdReactive {

    Mono<NoteResponse> execute(UUID id, NoteRequest request);
}
