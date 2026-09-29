package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateNoteByIdReactive {

    Mono<NoteResponse> execute(UUID id, NoteRequest request);
}
