package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUpdateUserNoteById {

    Mono<UserNoteResponse> updateUserNoteById(UUID id, UserNoteRequest request);
}
