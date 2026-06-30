package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserExistsByIdContractReactive;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserExistsByIdMongoReactiveAdapter implements UserExistsByIdContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    UserExistsByIdMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return this.userMongoReactiveRepository.existsById(id);
    }

}
