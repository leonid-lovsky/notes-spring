package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreateUserNoteById {

    Mono<UserNoteResponse> createUserNoteById(UUID id, UserNoteRequest request);
}
