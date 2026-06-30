package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdMongoReactiveAdapter implements UserNoteFindByUserIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteFindByUserIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteMongoReactiveRepository.findByIdUserId(userId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

}
