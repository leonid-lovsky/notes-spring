package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindNoteByIdReactive {

    Mono<NoteResponse> execute(UUID id);
}
