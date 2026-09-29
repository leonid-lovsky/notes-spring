package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateUserNoteByIdReactive {

    Mono<UserNoteResponse> execute(UUID id, UserNoteRequest request);
}
