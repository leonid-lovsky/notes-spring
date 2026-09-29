package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteUserNoteByIdReactive {

    Mono<UserNoteResponse> execute(UUID id);
}
