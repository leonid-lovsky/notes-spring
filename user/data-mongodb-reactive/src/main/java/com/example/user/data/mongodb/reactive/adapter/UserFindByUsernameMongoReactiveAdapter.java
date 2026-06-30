package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserFindByUsernameContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserReactiveDocumentMapperContract;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserFindByUsernameMongoReactiveAdapter implements UserFindByUsernameContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserReactiveDocumentMapperContract userReactiveDocumentMapper;

    UserFindByUsernameMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserReactiveDocumentMapperContract userReactiveDocumentMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userReactiveDocumentMapper = userReactiveDocumentMapper;
    }

    @Override
    public Mono<UserResponse> findByUsername(String username) {
        return this.userMongoReactiveRepository.findByUsername(username)
            .map(this.userReactiveDocumentMapper::toResponse);
    }

}
