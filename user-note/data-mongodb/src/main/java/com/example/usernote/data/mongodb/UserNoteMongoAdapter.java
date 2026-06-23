package com.example.usernote.data.mongodb;

import com.example.usernote.domain.UserNote;
import com.example.usernote.domain.UserNoteRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class UserNoteMongoAdapter implements UserNoteRepository {

    private final UserNoteMongoRepository userNoteMongoRepository;
    private final MongoTemplate mongoTemplate;

    UserNoteMongoAdapter(UserNoteMongoRepository userNoteMongoRepository, MongoTemplate mongoTemplate) {
        this.userNoteMongoRepository = userNoteMongoRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public boolean existsByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteMongoRepository.existsById(new UserNoteKey(userId, noteId));
    }

    @Override
    public Optional<UserNote> findByUserIdAndNoteId(UUID userId, UUID noteId) {
        return userNoteMongoRepository.findById(new UserNoteKey(userId, noteId))
                .map(UserNoteMongoAdapter::toDomain);
    }

    @Override
    public List<UserNote> findByUserId(UUID userId) {
        return userNoteMongoRepository.findByIdUserId(userId).stream()
                .map(UserNoteMongoAdapter::toDomain)
                .toList();
    }

    @Override
    public List<UserNote> findByNoteId(UUID noteId) {
        return userNoteMongoRepository.findByIdNoteId(noteId).stream()
                .map(UserNoteMongoAdapter::toDomain)
                .toList();
    }

    @Override
    public void add(UserNote userNote) {
        mongoTemplate.insert(toDocument(userNote));
    }

    @Override
    public void replace(UserNote userNote) {
        mongoTemplate.save(toDocument(userNote));
    }

    @Override
    public void remove(UUID userId, UUID noteId) {
        userNoteMongoRepository.deleteById(new UserNoteKey(userId, noteId));
    }

    private static UserNote toDomain(UserNoteDocument document) {
        return new UserNote(document.getId().getUserId(), document.getId().getNoteId(), document.getRole());
    }

    private static UserNoteDocument toDocument(UserNote userNote) {
        return new UserNoteDocument(new UserNoteKey(userNote.userId(), userNote.noteId()), userNote.role());
    }
}
