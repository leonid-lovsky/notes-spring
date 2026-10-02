package com.example.user;

import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService implements FindUserById, CreateUserById, ReplaceUserById, UpdateUserById, DeleteUserById {

    @Override
    public Mono<UserResponse> findUserById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> createUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> replaceUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> updateUserById(UUID id, UserRequest request) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }

    @Override
    public Mono<UserResponse> deleteUserById(UUID id) {
        return Mono.error(new UnsupportedOperationException("Not implemented"));
    }
}
