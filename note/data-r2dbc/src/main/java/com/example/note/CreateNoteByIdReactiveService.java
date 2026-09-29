package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateNoteByIdReactiveService implements CreateNoteByIdReactive {

    @Override
    public Mono<NoteResponse> execute(UUID id, NoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
