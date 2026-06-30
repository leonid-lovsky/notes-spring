package com.example.user.data.r2dbc.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserExistsByIdContractReactive;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdR2dbcAdapter implements UserExistsByIdContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    UserExistsByIdR2dbcAdapter(UserR2dbcRepository userR2dbcRepository) {
        this.userR2dbcRepository = userR2dbcRepository;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.userR2dbcRepository.existsById(id);
    }

}
