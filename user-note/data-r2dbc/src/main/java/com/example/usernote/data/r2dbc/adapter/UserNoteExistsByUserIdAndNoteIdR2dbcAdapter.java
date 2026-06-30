package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteExistsByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdR2dbcAdapter implements UserNoteExistsByUserIdAndNoteIdContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    UserNoteExistsByUserIdAndNoteIdR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.existsByUserIdAndNoteId(userId, noteId);
    }

}
