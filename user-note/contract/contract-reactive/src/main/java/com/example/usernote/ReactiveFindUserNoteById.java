package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveFindUserNoteById {

    Mono<UserNoteResponseBody> findUserNoteById(UUID id);
}
