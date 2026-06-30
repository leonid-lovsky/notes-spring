package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindByUsernameContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameMongoReactiveAdapter implements UserFindByUsernameContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserFindByUsernameMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Mono<UserResponse> findByUsername(String username) {
        return this.userMongoReactiveRepository.findByUsername(username).map(this.userMongoReactiveMapper::toResponse);
    }

}
