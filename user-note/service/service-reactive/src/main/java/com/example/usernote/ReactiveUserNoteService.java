package com.example.usernote;

import java.util.UUID;
import reactor.core.publisher.Mono;

public class ReactiveUserNoteService implements ReactiveFindUserNoteById, ReactiveCreateUserNoteById, ReactiveReplaceUserNoteById, ReactiveUpdateUserNoteById, ReactiveDeleteUserNoteById {

    private final ReactiveUserNoteRepository userNoteRepository;

    public ReactiveUserNoteService(ReactiveUserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public Mono<UserNoteResponse> findUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> createUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> replaceUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> updateUserNoteById(UUID id, UserNoteRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserNoteResponse> deleteUserNoteById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
