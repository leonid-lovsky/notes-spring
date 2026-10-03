package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveDeleteNoteById {

    Mono<NoteResponse> deleteNoteById(UUID id);
}
