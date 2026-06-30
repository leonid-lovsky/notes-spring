package com.example.user.data.r2dbc.adapter;

import com.example.user.contract.reactive.UserFindAllContractReactive;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllR2dbcAdapter implements UserFindAllContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserFindAllR2dbcAdapter(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userR2dbcRepository.findAll().map(this.userR2dbcMapper::toResponse);
    }

}
