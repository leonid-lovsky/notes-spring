package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdR2dbcAdapter implements UserNoteFindByUserIdContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteFindByUserIdR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByUserId(UUID userId) {
        return this.userNoteR2dbcRepository.findByUserId(userId).map(this.userNoteR2dbcMapper::toResponse);
    }

}
