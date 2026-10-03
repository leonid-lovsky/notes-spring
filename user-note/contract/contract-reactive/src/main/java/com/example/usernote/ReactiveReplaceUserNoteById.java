package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveReplaceUserNoteById {

    Mono<UserNoteResponse> replaceUserNoteById(UUID id, UserNoteRequest request);
}
