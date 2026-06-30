package com.example.usernote.data.r2dbc.adapter;

import com.example.usernote.contract.reactive.UserNoteAddContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.data.r2dbc.repository.UserNoteR2dbcRepository;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddR2dbcAdapter implements UserNoteAddContractReactive {

    private final UserNoteR2dbcRepository userNoteR2dbcRepository;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteAddR2dbcAdapter(UserNoteR2dbcRepository userNoteR2dbcRepository,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.userNoteR2dbcRepository = userNoteR2dbcRepository;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.userNoteR2dbcRepository.save(this.userNoteR2dbcMapper.toNewEntity(request))
            .map(this.userNoteR2dbcMapper::toResponse);
    }

}
