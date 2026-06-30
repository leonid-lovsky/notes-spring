package com.example.usernote.data.r2dbc.adapter;

import java.util.UUID;

import com.example.usernote.contract.reactive.UserNoteReplaceContractReactive;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteReplaceR2dbcAdapter implements UserNoteReplaceContractReactive {

    private final DatabaseClient databaseClient;

    UserNoteReplaceR2dbcAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<UserNoteResponse> replace(UUID userId, UUID noteId, UserNoteRequest request) {
        return this.databaseClient
            .sql("UPDATE user_notes SET role = :role WHERE user_id = :userId AND note_id = :noteId")
            .bind("userId", userId)
            .bind("noteId", noteId)
            .bind("role", request.role().name())
            .fetch()
            .rowsUpdated()
            .thenReturn(new UserNoteResponse(userId, noteId, request.role()));
    }

}
