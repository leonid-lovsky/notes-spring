package com.example.note;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReplaceNoteById {

    Mono<NoteResponse> replaceNoteById(UUID id, NoteRequest request);
}
