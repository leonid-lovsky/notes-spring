package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteRemoveContractReactive;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveMongoReactiveAdapter implements UserNoteRemoveContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    UserNoteRemoveMongoReactiveAdapter(UserNoteMongoReactiveRepository userNoteMongoReactiveRepository) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
    }

    @Override
    public Mono<Void> remove(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

}
