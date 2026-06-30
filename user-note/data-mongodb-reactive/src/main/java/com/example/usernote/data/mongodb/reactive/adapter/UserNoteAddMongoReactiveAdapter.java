package com.example.usernote.data.mongodb.reactive.adapter;

import com.example.usernote.contract.reactive.UserNoteAddContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddMongoReactiveAdapter implements UserNoteAddContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteAddMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteMongoReactiveRepository.insert(this.userNoteMongoReactiveMapper.toNewDocument(request))
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

}
