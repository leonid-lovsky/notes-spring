package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindAllContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserReactiveDocumentMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserFindAllMongoReactiveAdapter implements UserFindAllContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserReactiveDocumentMapperContract userReactiveDocumentMapper;

    UserFindAllMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserReactiveDocumentMapperContract userReactiveDocumentMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userReactiveDocumentMapper = userReactiveDocumentMapper;
    }

    @Override
    public Flux<UserResponse> findAll() {
        return this.userMongoReactiveRepository.findAll().map(this.userReactiveDocumentMapper::toResponse);
    }

}
