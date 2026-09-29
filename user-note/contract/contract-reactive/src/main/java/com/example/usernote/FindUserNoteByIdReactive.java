package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindUserNoteByIdReactive {

    Mono<UserNoteResponse> execute(UUID id);
}
