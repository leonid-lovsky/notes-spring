package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ReactiveDeleteUserNoteById {

    Mono<UserNoteResponse> deleteUserNoteById(UUID id);
}
