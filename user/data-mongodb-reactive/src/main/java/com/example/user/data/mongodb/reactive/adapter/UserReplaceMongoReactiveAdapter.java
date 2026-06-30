package com.example.user.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.user.contract.reactive.UserReplaceContractReactive;
import com.example.user.data.mongodb.reactive.mapper.UserMongoReactiveMapperContract;
import com.example.user.data.mongodb.reactive.model.UserReactiveDocument;
import com.example.user.data.mongodb.reactive.repository.UserMongoReactiveRepository;
import com.example.user.domain.UserRequest;
import com.example.user.domain.UserResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserReplaceMongoReactiveAdapter implements UserReplaceContractReactive {

    private final UserMongoReactiveRepository userMongoReactiveRepository;

    private final UserMongoReactiveMapperContract userMongoReactiveMapper;

    UserReplaceMongoReactiveAdapter(UserMongoReactiveRepository userMongoReactiveRepository,
            UserMongoReactiveMapperContract userMongoReactiveMapper) {
        this.userMongoReactiveRepository = userMongoReactiveRepository;
        this.userMongoReactiveMapper = userMongoReactiveMapper;
    }

    @Override
    public Mono<UserResponse> replace(UUID id, UserRequest request) {
        UserReactiveDocument document = this.userMongoReactiveMapper.toExistingDocument(id, request);
        return this.userMongoReactiveRepository.save(document).map(this.userMongoReactiveMapper::toResponse);
    }

}
