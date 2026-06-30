package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserFindByIdContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdMongoReactiveAdapter implements UserFindByIdContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserFindByIdMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Mono<UserResponse> findById(UUID id) {
        return this.userMongoReactiveRepository.findById(id).map(this.userMongoReactiveMapper::toResponse);
    }

}
