package com.example.user.data.r2dbc.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserRemoveContractReactive;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserRemoveR2dbcAdapter implements UserRemoveContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    UserRemoveR2dbcAdapter(UserR2dbcRepository userR2dbcRepository) {
        this.userR2dbcRepository = userR2dbcRepository;
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.userR2dbcRepository.deleteById(id);
    }

}
