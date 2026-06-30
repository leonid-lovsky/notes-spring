package com.example.user.data.mongodb.reactive.adapter;

import com.example.user.contract.reactive.UserAddContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserReactiveDocumentMapperContract;
import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserAddMongoReactiveAdapter implements UserAddContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserReactiveDocumentMapperContract userReactiveDocumentMapper;

    UserAddMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserReactiveDocumentMapperContract userReactiveDocumentMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userReactiveDocumentMapper = userReactiveDocumentMapper;
    }

    @Override
    public Mono<UserResponse> add(UserRequest request) {
        UserReactiveDocument document = this.userReactiveDocumentMapper.toNewDocument(request);
        return this.userMongoReactiveRepository.insert(document).map(this.userReactiveDocumentMapper::toResponse);
    }

}
