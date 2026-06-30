package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteRemoveContractReactive;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveR2dbcAdapter implements UserNoteRemoveContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    UserNoteRemoveR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
    }

    @Override
    public Mono<Void> remove(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.deleteByUserIdAndNoteId(userId, noteId);
    }

}
