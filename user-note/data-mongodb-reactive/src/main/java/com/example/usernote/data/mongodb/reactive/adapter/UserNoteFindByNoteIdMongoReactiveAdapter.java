package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByNoteIdContractReactive;
import com.example.usernote.data.mongodb.reactive.mapper.UserNoteMongoReactiveMapperContract;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdMongoReactiveAdapter implements UserNoteFindByNoteIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    private final UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper;

    UserNoteFindByNoteIdMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository,
            UserNoteMongoReactiveMapperContract userNoteMongoReactiveMapper) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
        this.userNoteMongoReactiveMapper = userNoteMongoReactiveMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.userNoteMongoReactiveRepository.findByIdNoteId(noteId)
            .map(this.userNoteMongoReactiveMapper::toResponse);
    }

}
