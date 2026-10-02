package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateNoteById {

    Mono<NoteResponse> createNoteById(UUID id, NoteRequest request);
}
