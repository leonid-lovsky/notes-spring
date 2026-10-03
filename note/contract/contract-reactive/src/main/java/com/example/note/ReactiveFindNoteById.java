package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveFindNoteById {

    Mono<NoteResponse> findNoteById(UUID id);
}
