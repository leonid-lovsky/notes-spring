package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteUserNoteById {

    Mono<UserNoteResponse> deleteUserNoteById(UUID id);
}
