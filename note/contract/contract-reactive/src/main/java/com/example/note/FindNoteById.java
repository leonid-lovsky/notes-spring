package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindNoteById {

    Mono<NoteResponse> findNoteById(UUID id);
}
