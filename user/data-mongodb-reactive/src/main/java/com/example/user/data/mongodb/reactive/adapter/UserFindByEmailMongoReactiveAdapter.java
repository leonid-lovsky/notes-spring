package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindByEmailContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserReactiveDocumentMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByEmailMongoReactiveAdapter implements UserFindByEmailContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserReactiveDocumentMapperContract userReactiveDocumentMapper;

    UserFindByEmailMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserReactiveDocumentMapperContract userReactiveDocumentMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userReactiveDocumentMapper = userReactiveDocumentMapper;
    }

    @Override
    public Mono<UserResponse> findByEmail(String email) {
        return this.userMongoReactiveRepository.findByEmail(email).map(this.userReactiveDocumentMapper::toResponse);
    }

}
