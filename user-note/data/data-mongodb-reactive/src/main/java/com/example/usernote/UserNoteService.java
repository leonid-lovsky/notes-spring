package com.example.usernote;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserNoteService implements FindUserNoteById, CreateUserNoteById, ReplaceUserNoteById, UpdateUserNoteById, DeleteUserNoteById {

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
