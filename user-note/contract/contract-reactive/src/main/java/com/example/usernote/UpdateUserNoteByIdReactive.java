package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateUserNoteByIdReactive {

    Mono<UserNoteResponse> execute(UUID id, UserNoteRequest request);
}
