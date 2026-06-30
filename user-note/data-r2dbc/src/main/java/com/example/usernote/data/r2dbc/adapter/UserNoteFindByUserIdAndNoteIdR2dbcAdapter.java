package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByUserIdAndNoteIdContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteR2dbcMapperContract;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByUserIdAndNoteIdR2dbcAdapter implements UserNoteFindByUserIdAndNoteIdContractReactive {

    private final DatabaseClient databaseClient;

    private final UserNoteR2dbcMapperContract userNoteR2dbcMapper;

    UserNoteFindByUserIdAndNoteIdR2dbcAdapter(DatabaseClient databaseClient,
            UserNoteR2dbcMapperContract userNoteR2dbcMapper) {
        this.databaseClient = databaseClient;
        this.userNoteR2dbcMapper = userNoteR2dbcMapper;
    }

    @Override
    public Mono<UserNoteResponse> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.databaseClient
            .sql("SELECT user_id, note_id, role FROM user_notes WHERE user_id = :userId AND note_id = :noteId")
            .bind("userId", userId)
            .bind("noteId", noteId)
            .map(this.userNoteR2dbcMapper::fromRow)
            .first();
    }

}
