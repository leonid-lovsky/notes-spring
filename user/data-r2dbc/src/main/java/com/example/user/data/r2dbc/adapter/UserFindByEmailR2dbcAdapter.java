package com.example.user.data.r2dbc.adapter;

import com.example.user.contract.reactive.UserFindByEmailContractReactive;
import com.example.user.data.r2dbc.mapper.UserR2dbcMapperContract;
import com.example.user.data.r2dbc.repository.UserR2dbcRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailR2dbcAdapter implements UserFindByEmailContractReactive {

    private final UserR2dbcRepository userR2dbcRepository;

    private final UserR2dbcMapperContract userR2dbcMapper;

    UserFindByEmailR2dbcAdapter(UserR2dbcRepository userR2dbcRepository, UserR2dbcMapperContract userR2dbcMapper) {
        this.userR2dbcRepository = userR2dbcRepository;
        this.userR2dbcMapper = userR2dbcMapper;
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userR2dbcRepository.findByEmail(email).map(this.userR2dbcMapper::toResponse);
    }

}
