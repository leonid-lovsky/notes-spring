package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindByEmailContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailMongoReactiveAdapter implements UserFindByEmailContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserFindByEmailMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userMongoReactiveRepository.findByEmail(email).map(this.userMongoReactiveMapper::toResponse);
    }

}
