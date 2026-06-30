package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserFindByIdContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserReactiveDocumentMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByIdMongoReactiveAdapter implements UserFindByIdContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserReactiveDocumentMapperContract userReactiveDocumentMapper;

    UserFindByIdMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserReactiveDocumentMapperContract userReactiveDocumentMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userReactiveDocumentMapper = userReactiveDocumentMapper;
    }

    @Override
    public Mono<UserResponse> findById(UUID id) {
        return this.userMongoReactiveRepository.findById(id).map(this.userReactiveDocumentMapper::toResponse);
    }

}
