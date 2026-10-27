package com.example.usernote;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class R2dbcUserNoteService implements ReactiveUserNoteService {

    private final R2dbcEntityTemplate entityTemplate;

    public R2dbcUserNoteService(R2dbcEntityTemplate entityTemplate) {
        this.entityTemplate = entityTemplate;
    }

    @Override
    public Mono<UserNoteResponseBody> findUserNoteById(UUID id) {
        Query query = Query.query(Criteria.where("id").is(id));
        Mono<UserNote> found = entityTemplate.selectOne(query, UserNote.class);
        Mono<UserNote> entity = found.switchIfEmpty(Mono.error(() -> new UserNoteNotFoundException(id)));
        return entity.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> createUserNote(UserNoteRequestBody requestBody) {
        UUID id = UUID.randomUUID();
        UserNote entity = new UserNote(id, requestBody.userId(), requestBody.noteId(), requestBody.role());
        Mono<UserNote> inserted = entityTemplate.insert(entity);
        return inserted.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> updateUserNoteRoleById(UUID id, UserNoteRole role) {
        Mono<UserNoteResponseBody> found = findUserNoteById(id);
        Mono<UserNote> updated = found.flatMap(body -> entityTemplate.update(new UserNote(body.id(), body.userId(), body.noteId(), role)));
        return updated.map(UserNote::responseBody);
    }

    @Override
    public Mono<UserNoteResponseBody> deleteUserNoteById(UUID id) {
        Mono<UserNoteResponseBody> found = findUserNoteById(id);
        Query query = Query.query(Criteria.where("id").is(id));
        Mono<Long> deleted = entityTemplate.delete(query, UserNote.class);
        return found.flatMap(deleted::thenReturn);
    }
}
