package com.example.user.data.r2dbc.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserServiceReactiveInterface;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserNotFoundException;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserService implements UserServiceReactiveInterface {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserService(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.userR2dbcRepository.existsById(id);
    }

    @Override
    public Mono<UserResponse> add(UserRequest request) {
        return this.userR2dbcRepository.save(this.userR2dbcMapper.toNewEntity(request))
            .map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userR2dbcRepository.findAll().map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findById(UUID id) {
        return this.userR2dbcRepository.findById(id)
            .map(this.userR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userR2dbcRepository.findByEmail(email)
            .map(this.userR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNotFoundException(email)));
    }

    @Override
    public Mono<UserResponse> findByUsername(String username) {
        return this.userR2dbcRepository.findByUsername(username)
            .map(this.userR2dbcMapper::toResponse)
            .switchIfEmpty(Mono.error(new UserNotFoundException(username)));
    }

    @Override
    public Mono<UserResponse> replace(UUID id, UserRequest request) {
        return this.userR2dbcRepository.existsById(id)
            .flatMap((exists) -> exists
                    ? this.userR2dbcRepository.save(this.userR2dbcMapper.toExistingEntity(id, request))
                        .map(this.userR2dbcMapper::toResponse)
                    : Mono.error(new UserNotFoundException(id)));
    }

    @Override
    public Mono<UserResponse> merge(UUID id, UserRequest request) {
        return findById(id).flatMap((existing) -> {
            UserRequest merged = merge(existing, request);
            return this.userR2dbcRepository.save(this.userR2dbcMapper.toExistingEntity(id, merged))
                .map(this.userR2dbcMapper::toResponse);
        });
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.userR2dbcRepository.existsById(id)
            .flatMap((exists) -> exists ? this.userR2dbcRepository.deleteById(id)
                    : Mono.error(new UserNotFoundException(id)));
    }

    private static UserRequest merge(UserResponse existing, UserRequest request) {
        String username = (request.username() != null) ? request.username() : existing.username();
        String email = (request.email() != null) ? request.email() : existing.email();
        return new UserRequest(username, email);
    }
}
