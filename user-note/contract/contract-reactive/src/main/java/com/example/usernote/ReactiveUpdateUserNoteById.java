package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveUpdateUserNoteById {

    Mono<UserNoteResponse> updateUserNoteById(UUID id, UserNoteRequest request);
}
