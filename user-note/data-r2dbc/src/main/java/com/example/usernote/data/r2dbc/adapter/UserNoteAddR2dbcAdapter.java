package com.example.usernote.data.r2dbc.adapter;

import com.example.usernote.contract.reactive.UserNoteAddContractReactive;
import com.example.usernote.domain.UserNoteRequest;
import com.example.usernote.domain.UserNoteResponse;
import reactor.core.publisher.Mono;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

@Repository
class UserNoteAddR2dbcAdapter implements UserNoteAddContractReactive {

    private final DatabaseClient databaseClient;

    UserNoteAddR2dbcAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<UserNoteResponse> add(UserNoteRequest request) {
        return this.databaseClient
            .sql("INSERT INTO user_notes (user_id, note_id, role) VALUES (:userId, :noteId, :role)")
            .bind("userId", request.userId())
            .bind("noteId", request.noteId())
            .bind("role", request.role().name())
            .fetch()
            .rowsUpdated()
            .thenReturn(new UserNoteResponse(request.userId(), request.noteId(), request.role()));
    }

}
