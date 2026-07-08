package com.example.user.data.r2dbc.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserContractReactive;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserR2dbcAdapter implements UserContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserR2dbcAdapter(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Mono<UserResponse> add(UserRequest request) {
        return this.userR2dbcRepository.save(this.userR2dbcMapper.toNewEntity(request))
            .map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.userR2dbcRepository.existsById(id);
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userR2dbcRepository.findAll().map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userR2dbcRepository.findByEmail(email).map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findById(UUID id) {
        return this.userR2dbcRepository.findById(id).map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findByUsername(String username) {
        return this.userR2dbcRepository.findByUsername(username).map(this.userR2dbcMapper::toResponse);
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.userR2dbcRepository.deleteById(id);
    }

    @Override
    public Mono<UserResponse> replace(UUID id, UserRequest request) {
        return this.userR2dbcRepository.save(this.userR2dbcMapper.toExistingEntity(id, request))
            .map(this.userR2dbcMapper::toResponse);
    }

}
