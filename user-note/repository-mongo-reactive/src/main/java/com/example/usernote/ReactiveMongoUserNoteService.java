package com.example.usernote;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class ReactiveMongoUserNoteService implements ReactiveUserNoteService {

    private final ReactiveMongoOperations mongoOperations;

    public ReactiveMongoUserNoteService(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Mono<UserNoteResponseBody> findUserNoteById(UUID id) {
        Mono<UserNote> found = mongoOperations.findById(id, UserNote.class);
        Mono<UserNote> entity = found.switchIfEmpty(Mono.error(() -> new UserNoteNotFoundException(id)));
        return entity.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody requestBody) {
        UUID id = UUID.randomUUID();
        UserNote entity = new UserNote(id, requestBody.userId(), requestBody.noteId(), requestBody.role());
        Mono<UserNote> inserted = mongoOperations.insert(entity);
        return inserted.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("role", role);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        Mono<UserNote> updated = mongoOperations.findAndModify(query, update, options, UserNote.class);
        Mono<UserNote> entity = updated.switchIfEmpty(Mono.error(() -> new UserNoteNotFoundException(id)));
        return entity.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> deleteUserNoteById(UUID id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Mono<UserNote> removed = mongoOperations.findAndRemove(query, UserNote.class);
        Mono<UserNote> entity = removed.switchIfEmpty(Mono.error(() -> new UserNoteNotFoundException(id)));
        return entity.map(UserNote::responseBody);
    }
}
