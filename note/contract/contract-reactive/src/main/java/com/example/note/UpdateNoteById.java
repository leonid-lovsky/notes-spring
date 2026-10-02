package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateNoteById {

    Mono<NoteResponse> updateNoteById(UUID id, NoteRequest request);
}
