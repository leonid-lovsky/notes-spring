package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FindNoteByIdReactiveService implements FindNoteByIdReactive {

    @Override
    public Mono<NoteResponse> execute(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
