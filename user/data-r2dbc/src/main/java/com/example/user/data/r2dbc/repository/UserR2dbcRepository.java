package com.example.user.data.r2dbc.repository;

import java.util.UUID;

import com.example.user.data.r2dbc.model.UserR2dbcEntity;
import reactor.core.publisher.Mono;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserR2dbcRepository extends ReactiveCrudRepository<UserR2dbcEntity, UUID> {

    Mono<UserR2dbcEntity> findByUsername(String username);

    Mono<UserR2dbcEntity> findByEmail(String email);

}
