package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveCreateUserNoteById {

    Mono<UserNoteResponse> createUserNoteById(UUID id, UserNoteRequest request);
}
