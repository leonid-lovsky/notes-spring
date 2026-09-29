package com.example.note;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteNoteByIdReactiveService implements DeleteNoteByIdReactive {

    @Override
    public Mono<NoteResponse> execute(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
