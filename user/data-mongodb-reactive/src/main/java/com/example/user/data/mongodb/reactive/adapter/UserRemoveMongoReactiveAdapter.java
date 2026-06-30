package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserRemoveContractReactive;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserRemoveMongoReactiveAdapter implements UserRemoveContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    UserRemoveMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
    }

    @Override
    public Mono<Void> remove(UUID id) {
        return this.userMongoReactiveRepository.deleteById(id);
    }

}
