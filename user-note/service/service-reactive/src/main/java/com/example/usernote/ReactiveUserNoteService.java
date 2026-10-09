package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class ReactiveUserNoteService implements ReactiveFindUserNoteById, ReactiveCreateUserNote, ReactiveUpdateUserNoteRoleById, ReactiveDeleteUserNoteById {

    private final ReactiveUserNoteRepository userNoteRepository;

    public ReactiveUserNoteService(ReactiveUserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public Mono<UserNoteResponseBody> findUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody requestBody) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponseBody> deleteUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
