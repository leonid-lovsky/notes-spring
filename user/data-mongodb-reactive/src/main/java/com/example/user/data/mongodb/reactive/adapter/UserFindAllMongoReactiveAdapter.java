package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindAllContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllMongoReactiveAdapter implements UserFindAllContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserFindAllMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userMongoReactiveRepository.findAll().map(this.userMongoReactiveMapper::toResponse);
    }

}
