package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteFindByNoteIdContractReactive;
import com.example.usernote.data.r2dbc.mapper.UserNoteRowMapperContract;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Flux;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteFindByNoteIdR2dbcAdapter implements UserNoteFindByNoteIdContractReactive {

    private final DatabaseClient databaseClient;

    private final UserNoteRowMapperContract userNoteRowMapper;

    UserNoteFindByNoteIdR2dbcAdapter(DatabaseClient databaseClient, UserNoteRowMapperContract userNoteRowMapper) {
        this.databaseClient = databaseClient;
        this.userNoteRowMapper = userNoteRowMapper;
    }

    @Override
    public Flux<UserNoteResponse> findByNoteId(UUID noteId) {
        return this.databaseClient.sql("SELECT user_id, note_id, role FROM user_notes WHERE note_id = :noteId")
            .bind("noteId", noteId)
            .map(this.userNoteRowMapper::fromRow)
            .all();
    }

}
