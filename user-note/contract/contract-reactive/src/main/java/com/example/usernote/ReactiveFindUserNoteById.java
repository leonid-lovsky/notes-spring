package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveFindUserNoteById {

    Mono<UserNoteResponse> findUserNoteById(UUID id);
}
