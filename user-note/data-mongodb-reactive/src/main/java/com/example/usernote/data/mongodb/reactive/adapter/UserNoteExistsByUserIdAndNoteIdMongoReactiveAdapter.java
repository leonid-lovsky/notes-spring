package com.example.usernote.data.mongodb.reactive.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteExistsByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.mongodb.reactive.model.UserNoteReactiveKey;
import com.example.usernote.data.mongodb.reactive.repository.UserNoteMongoReactiveRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdMongoReactiveAdapter implements UserNoteExistsByUserIdAndNoteIdContractReactive {

    private final UserNoteMongoReactiveRepository userNoteMongoReactiveRepository;

    UserNoteExistsByUserIdAndNoteIdMongoReactiveAdapter(
            UserNoteMongoReactiveRepository userNoteMongoReactiveRepository) {
        this.userNoteMongoReactiveRepository = userNoteMongoReactiveRepository;
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteMongoReactiveRepository.existsById(new UserNoteReactiveKey(userId, noteId));
    }

}
