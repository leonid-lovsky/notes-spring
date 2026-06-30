package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdR2dbcAdapter implements UserNoteFindByUserIdAndNoteIdContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteFindByUserIdAndNoteIdR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .map(this.userNoteR2dbcMapper::toResponse);
    }

}
