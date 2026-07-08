package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserMongoReactiveAdapter implements UserContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Mono<UserResponse> add(UserRequest request) {
        UserReactiveDocument document = this.userMongoReactiveMapper.toNewDocument(request);
        return this.userMongoReactiveRepository.insert(document).map(this.userMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.userMongoReactiveRepository.existsById(id);
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userMongoReactiveRepository.findAll().map(this.userMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userMongoReactiveRepository.findByEmail(email).map(this.userMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findById(UUID id) {
        return this.userMongoReactiveRepository.findById(id).map(this.userMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<UserResponse> findByUsername(String username) {
        return this.userMongoReactiveRepository.findByUsername(username).map(this.userMongoReactiveMapper::toResponse);
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.userMongoReactiveRepository.deleteById(id);
    }

    @Override
    public Mono<UserResponse> replace(UUID id, UserRequest request) {
        UserReactiveDocument document = this.userMongoReactiveMapper.toExistingDocument(id, request);
        return this.userMongoReactiveRepository.save(document).map(this.userMongoReactiveMapper::toResponse);
    }

}
