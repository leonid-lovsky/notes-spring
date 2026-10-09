package com.example.usernote;

import reactor.core.publisher.Mono;

public interface ReactiveCreateUserNote {

    Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody request);
}
