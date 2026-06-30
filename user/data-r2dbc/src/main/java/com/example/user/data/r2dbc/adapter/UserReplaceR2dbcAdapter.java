package com.example.user.data.r2dbc.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserReplaceContractReactive;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserReplaceR2dbcAdapter implements UserReplaceContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserReplaceR2dbcAdapter(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Mono<UserResponse> replace(UUID id, UserRequest request) {
        return this.userR2dbcRepository.save(this.userR2dbcMapper.toExistingEntity(id, request))
            .map(this.userR2dbcMapper::toResponse);
    }

}
