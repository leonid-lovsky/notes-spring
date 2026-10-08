package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveDeleteUserNoteById {

    Mono<UserNoteResponse> deleteUserNoteById(UUID id);
}
