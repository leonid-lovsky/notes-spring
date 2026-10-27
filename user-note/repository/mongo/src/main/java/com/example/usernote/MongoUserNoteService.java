package com.example.usernote;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MongoUserNoteService implements UserNoteService {

    private final MongoOperations mongoOperations;

    public MongoUserNoteService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public UserNoteResponseBody findUserNoteById(UUID id) {
        UserNote entity = mongoOperations.findById(id, UserNote.class);
        if (entity == null) {
            throw new UserNoteNotFoundException(id);
        }
        return entity.responseBody();
    }

    @Override
    public UserNoteResponseBody createUserNote(UserNoteRequestBody requestBody) {
        UUID id = UUID.randomUUID();
        UserNote entity = new UserNote(id, requestBody.userId(), requestBody.noteId(), requestBody.role());
        UserNote inserted = mongoOperations.insert(entity);
        return inserted.responseBody();
    }

    @Override
    public UserNoteResponseBody updateUserNoteRoleById(UUID id, UserNoteRole role) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = Update.update("role", role);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
        UserNote updated = mongoOperations.findAndModify(query, update, options, UserNote.class);
        if (updated == null) {
            throw new UserNoteNotFoundException(id);
        }
        return updated.responseBody();
    }

    @Override
    public UserNoteResponseBody deleteUserNoteById(UUID id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        UserNote removed = mongoOperations.findAndRemove(query, UserNote.class);
        if (removed == null) {
            throw new UserNoteNotFoundException(id);
        }
        return removed.responseBody();
    }
}
