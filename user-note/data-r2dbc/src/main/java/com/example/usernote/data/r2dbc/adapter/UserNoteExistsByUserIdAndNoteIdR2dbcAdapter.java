package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteExistsByUserIdAndNoteIdContractReactive;
import reactor.core.publisher.Mono;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteExistsByUserIdAndNoteIdR2dbcAdapter implements UserNoteExistsByUserIdAndNoteIdContractReactive {

    private final DatabaseClient databaseClient;

    UserNoteExistsByUserIdAndNoteIdR2dbcAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Boolean> existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return this.databaseClient
            .sql("SELECT COUNT(*) AS count FROM user_notes WHERE user_id = :userId AND note_id = :noteId")
            .bind("userId", userId)
            .bind("noteId", noteId)
            .map((row, rowMetadata) -> row.get("count", Long.class))
            .one()
            .map((count) -> count > 0);
    }

}
