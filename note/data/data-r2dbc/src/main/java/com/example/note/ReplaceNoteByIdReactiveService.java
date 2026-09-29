package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReplaceNoteByIdReactiveService implements ReplaceNoteByIdReactive {

    @Override
    public Mono<NoteResponse> execute(UUID id, NoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
