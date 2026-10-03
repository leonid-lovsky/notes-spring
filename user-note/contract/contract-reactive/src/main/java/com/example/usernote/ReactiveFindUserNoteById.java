package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveFindUserNoteById {

    Mono<UserNoteResponse> findUserNoteById(UUID id);
}
