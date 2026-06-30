package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteRemoveContractReactive;
import reactor.core.publisher.Mono;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteRemoveR2dbcAdapter implements UserNoteRemoveContractReactive {

    private final DatabaseClient databaseClient;

    UserNoteRemoveR2dbcAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Void> remove(UUID userId, UUID noteId) {
        return this.databaseClient.sql("DELETE FROM user_notes WHERE user_id = :userId AND note_id = :noteId")
            .bind("userId", userId)
            .bind("noteId", noteId)
            .fetch()
            .rowsUpdated()
            .then();
    }

}
