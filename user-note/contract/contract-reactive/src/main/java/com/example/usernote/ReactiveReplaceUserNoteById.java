package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveReplaceUserNoteById {

    Mono<UserNoteResponse> replaceUserNoteById(UUID id, UserNoteRequest request);
}
