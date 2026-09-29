package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteUserNoteByIdReactiveService implements DeleteUserNoteByIdReactive {

    @Override
    public Mono<UserNoteResponse> execute(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
