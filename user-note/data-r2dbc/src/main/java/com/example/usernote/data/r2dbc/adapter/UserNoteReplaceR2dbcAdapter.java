package com.example.usernote.data.r2dbc.adapter;

import java.util.Objects;
import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteReplaceContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.UserNoteNotFoundException;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceR2dbcAdapter implements UserNoteReplaceContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteReplaceR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.userNoteR2dbcRepository.findByUserIdAndNoteId(userId, noteId)
            .switchIfEmpty(Mono.error(new UserNoteNotFoundException(userId, noteId)))
            .flatMap((existing) -> this.userNoteR2dbcRepository
                .save(this.userNoteR2dbcMapper.toExistingEntity(Objects.requireNonNull(existing.getId()), request)))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

}
