package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ReactiveUserNoteService {

    Mono<UserNoteResponseBody> findUserNoteById(UUID id);

    Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody requestBody);

    Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role);

    Mono<UserNoteResponseBody> deleteUserNoteById(UUID id);
}
