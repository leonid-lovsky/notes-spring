package com.example.usernote;

import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public class JdbcUserNoteRepository implements UserNoteRepository {

    private final JdbcAggregateOperations aggregateOperations;

    public JdbcUserNoteRepository(JdbcAggregateOperations aggregateOperations) {
        this.aggregateOperations = aggregateOperations;
    }

    @Override
    public UserNoteResponseBody findUserNoteById(UUID id) {
        UserNote entity = aggregateOperations.findById(id, UserNote.class);
        if (entity == null) {
            throw new UserNoteNotFoundException(id);
        }
        return entity.responseBody();
    }

    @Override
    public UserNoteResponseBody createUserNote(UserNoteRequestBody requestBody) {
        UUID id = UUID.randomUUID();
        UserNote entity = new UserNote(id, requestBody.userId(), requestBody.noteId(), requestBody.role());
        UserNote inserted = aggregateOperations.insert(entity);
        return inserted.responseBody();
    }

    @Override
    public UserNoteResponseBody updateUserNoteRoleById(UUID id, UserNoteRole role) {
        UserNoteResponseBody found = findUserNoteById(id);
        UserNote entity = new UserNote(found.id(), found.userId(), found.noteId(), role);
        UserNote updated = aggregateOperations.update(entity);
        return updated.responseBody();
    }

    @Override
    public UserNoteResponseBody deleteUserNoteById(UUID id) {
        UserNoteResponseBody found = findUserNoteById(id);
        aggregateOperations.deleteById(id, UserNote.class);
        return found;
    }
}
