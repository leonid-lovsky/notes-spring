package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateUserNoteById {

    Mono<UserNoteResponse> updateUserNoteById(UUID id, UserNoteRequest request);
}
