package com.example.user.data.r2dbc.adapter;

import com.example.user.contract.reactive.UserAddContractReactive;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserAddR2dbcAdapter implements UserAddContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserAddR2dbcAdapter(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Mono<UserResponse> add(UserRequest request) {
        return this.userR2dbcRepository.save(this.userR2dbcMapper.toNewEntity(request))
            .map(this.userR2dbcMapper::toResponse);
    }

}
