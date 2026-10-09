package com.example.usernote;

import reactor.core.publisher.Mono;

import java.util.UUID;

public class ReactiveUserNoteServiceImpl implements ReactiveUserNoteService {

    private final ReactiveUserNoteRepository userNoteRepository;

    public ReactiveUserNoteServiceImpl(ReactiveUserNoteRepository userNoteRepository) {
        this.userNoteRepository = userNoteRepository;
    }

    @Override
    public Mono<UserNoteResponseBody> findUserNoteById(UUID id) {
        return userNoteRepository.findUserNoteById(id);
    }

    @Override
    public Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody requestBody) {
        return userNoteRepository.createUserNote(requestBody);
    }

    @Override
    public Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role) {
        return userNoteRepository.updateUserNoteRoleById(id, role);
    }

    @Override
    public Mono<UserNoteResponseBody> deleteUserNoteById(UUID id) {
        return userNoteRepository.deleteUserNoteById(id);
    }
}
