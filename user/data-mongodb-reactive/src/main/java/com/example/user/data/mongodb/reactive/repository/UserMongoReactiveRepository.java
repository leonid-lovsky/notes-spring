package com.example.user.data.mongodb.reactive.repository;

import java.util.UUID;

import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import reactor.core.publisher.Mono;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserMongoReactiveRepository extends ReactiveMongoRepository<UserReactiveDocument, UUID> {

    Mono<UserReactiveDocument> findByUsername(String username);

    Mono<UserReactiveDocument> findByEmail(String email);
}
