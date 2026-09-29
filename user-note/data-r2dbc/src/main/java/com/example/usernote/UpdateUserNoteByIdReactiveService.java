package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateUserNoteByIdReactiveService implements UpdateUserNoteByIdReactive {

    @Override
    public Mono<UserNoteResponse> execute(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
