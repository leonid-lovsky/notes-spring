package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteNoteByIdReactive {

    Mono<NoteResponse> execute(UUID id);
}
