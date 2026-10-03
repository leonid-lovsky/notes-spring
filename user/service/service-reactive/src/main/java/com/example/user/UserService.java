package com.example.user;

import java.util.UUID;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService implements ReactiveFindUserById, ReactiveCreateUserById, ReactiveReplaceUserById, ReactiveUpdateUserById, ReactiveDeleteUserById {

    private final ReactiveCrudRepository<User, UUID> userCrudRepository;
    private final ReactiveSortingRepository<User, UUID> userSortingRepository;
    private final ReactiveQueryByExampleExecutor<User> userQueryByExampleExecutor;

    public UserService(
        ReactiveCrudRepository<User, UUID> userCrudRepository,
        ReactiveSortingRepository<User, UUID> userSortingRepository,
        ReactiveQueryByExampleExecutor<User> userQueryByExampleExecutor
    ) {
        this.userCrudRepository = userCrudRepository;
        this.userSortingRepository = userSortingRepository;
        this.userQueryByExampleExecutor = userQueryByExampleExecutor;
    }

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
